package com.supermarket.billingservice.exception;

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

    @Value("${spring.application.name}")
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
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        // Check if the cause suggests a downstream service issue that should be a 502/503
        if (ex.getCause() instanceof FeignException) {
            FeignException fe = (FeignException) ex.getCause();
            if (fe.status() == -1 || fe.status() >= 500) {
                status = HttpStatus.BAD_GATEWAY; // Or SERVICE_UNAVAILABLE
            }
        }
        Map<String, Object> body = createErrorBody(status, ex.getMessage(), request);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(OrderPlacementException.class)
    public ResponseEntity<Object> handleOrderPlacementException(OrderPlacementException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> body = createErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Object> handleFeignNotFoundException(FeignException.NotFound ex, WebRequest request) {
        // Context matters: Was it cart items, cart ID? The service method should provide context in the rethrown exception.
        String message = "Required resource not found in Cart Service. " + ex.getMessage();
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, message, request); // Respond with 404
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
        HttpStatus status;
        String downstreamServiceName = "Cart Service"; // Assuming it's cart service
        try { // Safely extract host if possible
            downstreamServiceName = ex.request().url().split("/")[2];
        } catch (Exception ignored) {}
        String message;

        if (ex.status() >= 500) {
            status = HttpStatus.BAD_GATEWAY;
            message = "Downstream service [" + downstreamServiceName + "] reported an error.";
        } else if (ex.status() > 0) {
            status = HttpStatus.resolve(ex.status()) != null ? HttpStatus.resolve(ex.status()) : HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Error communicating with downstream service [" + downstreamServiceName + "]: Client error " + ex.status();
        } else { // status = -1 (e.g., connection refused)
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Downstream service [" + downstreamServiceName + "] is unavailable.";
        }

        Map<String, Object> body = createErrorBody(status, message, request);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        String message = "A database error occurred while processing the order.";
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        String message = "An unexpected internal error occurred in the billing service.";
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
