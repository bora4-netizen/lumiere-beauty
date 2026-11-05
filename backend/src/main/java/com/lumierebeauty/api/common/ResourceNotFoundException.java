package com.lumierebeauty.api.common;

/** Thrown when a requested resource (e.g. a product or user) does not exist. Mapped to HTTP 404. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
