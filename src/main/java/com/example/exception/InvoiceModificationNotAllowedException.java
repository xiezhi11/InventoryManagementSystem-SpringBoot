package com.example.exception;

public class InvoiceModificationNotAllowedException extends RuntimeException {

    public InvoiceModificationNotAllowedException(String message) {
        super(message);
    }
}
