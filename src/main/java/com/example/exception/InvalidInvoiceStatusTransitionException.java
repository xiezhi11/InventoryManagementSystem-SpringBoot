package com.example.exception;

public class InvalidInvoiceStatusTransitionException extends RuntimeException {

    public InvalidInvoiceStatusTransitionException(String message) {
        super(message);
    }
}
