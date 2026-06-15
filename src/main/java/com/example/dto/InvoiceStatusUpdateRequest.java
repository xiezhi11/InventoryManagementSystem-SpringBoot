package com.example.dto;

import com.example.entity.InvoiceStatus;

/**
 * Request body for {@code PUT /invoices/{id}/status}.
 */
public class InvoiceStatusUpdateRequest {

    private InvoiceStatus status;

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}
