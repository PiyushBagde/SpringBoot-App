package com.supermarket.cartservice.exception;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${spring.application.name")
    private String serviceName;

    private Map<String, Object> createErrorBody(HttpStatus status, String message, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("service", serviceName);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<Object> handleOperationFailedException(OperationFailedException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CartOperationException.class)
    public ResponseEntity<Object> handleCartOperationException(CartOperationException ex, WebRequest request) {
        // Determine status based on the nature of the exception if needed, default to BAD_REQUEST
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = createErrorBody(status, ex.getMessage(), request);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.NotFound.class) // Specifically handle 404 from downstream service
    public ResponseEntity<Object> handleFeignNotFoundException(FeignException.NotFound ex, WebRequest request) {
        String message = "Required resource not found in downstream service. " + ex.getMessage();
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, message, request);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class) // Handle other Feign errors (like 5xx, connection refused)
    public ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
        HttpStatus status;
        String message;
        // Distinguish between server errors from downstream and connection issues
        if (ex.status() >= 500) {
            status = HttpStatus.BAD_GATEWAY; // Or SERVICE_UNAVAILABLE
            message = "Downstream service [" + ex.request().url().split("/")[2] + "] reported an error.";
        } else if (ex.status() > 0) { // Handle other client errors (4xx other than 404) if needed
            status = HttpStatus.resolve(ex.status()) != null ? HttpStatus.resolve(ex.status()) : HttpStatus.INTERNAL_SERVER_ERROR; // Resolve status or default
            message = "Error communicating with downstream service [" + ex.request().url().split("/")[2] + "]: Client error " + ex.status();
        }
        else { // status = -1 typically means connection refused or timeout
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Downstream service [" + ex.request().url().split("/")[2] + "] is unavailable.";
        }

        Map<String, Object> body = createErrorBody(status, message, request);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        String message = "A database error occurred. Please try again later.";
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        String message = "An unexpected internal error occurred in the cart service.";
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }








}
