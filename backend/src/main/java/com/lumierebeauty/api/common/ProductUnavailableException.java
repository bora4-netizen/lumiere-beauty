package com.lumierebeauty.api.common;

/** Thrown when an order references a product that is not available (e.g. status = Draft). Mapped to HTTP 400. */
public class ProductUnavailableException extends RuntimeException {
    public ProductUnavailableException(String message) {
        super(message);
    }
}
