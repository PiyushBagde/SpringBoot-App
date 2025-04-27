package com.supermarket.inventoryservice.exception;

public class ResourceAlreadyExistsException extends Throwable {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
