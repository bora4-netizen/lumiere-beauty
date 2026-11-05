package com.lumierebeauty.api.common;

/** Thrown when registering an email that already has an account. Mapped to HTTP 409. */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
