package com.example.exception;

/**
 * Thrown when an invoice status change is not a legal transition.
 */
public class InvalidInvoiceStatusTransitionException extends RuntimeException {

    public InvalidInvoiceStatusTransitionException(String message) {
        super(message);
    }
}
