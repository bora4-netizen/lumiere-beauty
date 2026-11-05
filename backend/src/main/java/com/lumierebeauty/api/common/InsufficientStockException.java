package com.lumierebeauty.api.common;

/** Thrown when an order asks for more units of a product than are in stock. Mapped to HTTP 400. */
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
