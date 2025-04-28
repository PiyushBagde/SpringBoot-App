package com.supermarket.userservice.exception;

import org.springframework.beans.factory.annotation.Value;
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
        body.put("service", serviceName); // Add the service name here
        body.put("path", request.getDescription(false).replace("uri=", "")); // Get request path
        return body;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        logger.warn(String.format("[%s] Resource not found: %s", serviceName, ex.getMessage())); // Add service name to log
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {

        Map<String, Object> body = createErrorBody(HttpStatus.CONFLICT, ex.getMessage(), request);
        logger.warn(String.format("[%s] User already exists conflict: %s", serviceName, ex.getMessage())); // Add service name to log
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Object> handleAuthenticationFailedException(AuthenticationFailedException ex, WebRequest request) {

        Map<String, Object> body = createErrorBody(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
        logger.warn(String.format("[%s] Authentication failed: %s", serviceName, ex.getMessage())); // Add service name to log
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<Object> handleOperationFailedException(OperationFailedException ex, WebRequest request) {

        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
        // Log the error with stack trace for internal server errors
        logger.error(String.format("[%s] Operation failed: %s", serviceName, ex.getMessage()), ex); // Add service name to log
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {

        String message = "An unexpected error occurred. Please try again later.";
        Map<String, Object> body = createErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
        // Log the full exception details for debugging
        logger.error(String.format("[%s] Unhandled exception occurred: %s", serviceName, ex.getMessage()), ex); // Add service name to log
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
