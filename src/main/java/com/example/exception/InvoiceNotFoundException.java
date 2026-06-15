package com.example.exception;

/**
 * Thrown when an invoice referenced by id does not exist.
 */
public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
