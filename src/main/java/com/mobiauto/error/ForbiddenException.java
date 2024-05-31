package com.mobiauto.error;

public class ForbiddenException extends RuntimeException {

    private final int statusCode;

    public ForbiddenException(String message) {
        super(message);
        this.statusCode = 403;
    }

    public int getStatusCode() {
        return statusCode;
    }
}