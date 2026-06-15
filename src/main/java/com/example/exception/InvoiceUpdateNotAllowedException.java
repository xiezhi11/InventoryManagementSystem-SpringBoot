package com.example.exception;

/**
 * Thrown when a normal update tries to change core fields (amounts / product
 * information) of an invoice that is already PAID or VOID.
 */
public class InvoiceUpdateNotAllowedException extends RuntimeException {

    public InvoiceUpdateNotAllowedException(String message) {
        super(message);
    }
}
