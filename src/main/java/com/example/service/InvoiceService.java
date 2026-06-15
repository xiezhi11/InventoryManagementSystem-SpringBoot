package com.example.service;

import com.example.dto.InvoiceStatusRequest;
import com.example.entity.Invoice;
import com.example.entity.InvoiceStatus;
import com.example.exception.InvalidInvoiceStatusTransitionException;
import com.example.exception.InvoiceModificationNotAllowedException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public void insert(Invoice invoice) {
        if (invoice.getStatus() == null) {
            invoice.setStatus(InvoiceStatus.DRAFT);
        }
        invoiceRepository.save(invoice);
    }

    public Optional<Invoice> findById(int id) {
        return invoiceRepository.findById(id);
    }

    public Iterable<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> findByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    public void updateInvoice(int id, Invoice invoice) {
        Invoice existing = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (existing.getStatus() == InvoiceStatus.PAID || existing.getStatus() == InvoiceStatus.VOIDED) {
            throw new InvoiceModificationNotAllowedException(
                "Cannot modify invoice with status: " + existing.getStatus()
            );
        }

        invoice.setInvoiceId(id);
        invoice.setStatus(existing.getStatus());
        invoice.setConfirmedAt(existing.getConfirmedAt());
        invoice.setPaidAt(existing.getPaidAt());
        invoice.setVoidedAt(existing.getVoidedAt());
        invoice.setStatusRemark(existing.getStatusRemark());
        invoice.setCreatedUser(existing.getCreatedUser());
        invoice.setCreatedDateTime(existing.getCreatedDateTime());

        invoiceRepository.save(invoice);
    }

    public void updateInvoiceStatus(int id, InvoiceStatusRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        InvoiceStatus currentStatus = invoice.getStatus();
        InvoiceStatus targetStatus = request.getTargetStatus();

        if (!isValidTransition(currentStatus, targetStatus)) {
            throw new InvalidInvoiceStatusTransitionException(
                String.format("Cannot transition from %s to %s", currentStatus, targetStatus)
            );
        }

        invoice.setStatus(targetStatus);
        invoice.setStatusRemark(request.getRemark());

        Date now = new Date();
        switch (targetStatus) {
            case CONFIRMED:
                invoice.setConfirmedAt(now);
                break;
            case PAID:
                invoice.setPaidAt(now);
                break;
            case VOIDED:
                invoice.setVoidedAt(now);
                break;
            default:
                break;
        }

        invoiceRepository.save(invoice);
    }

    public void deleteInvoice(int id) {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new InvoiceModificationNotAllowedException(
                "Only DRAFT invoices can be deleted. Current status: " + invoice.getStatus()
            );
        }

        invoiceRepository.delete(invoice);
    }

    private boolean isValidTransition(InvoiceStatus from, InvoiceStatus to) {
        if (from == to) {
            return false;
        }

        switch (from) {
            case DRAFT:
                return to == InvoiceStatus.CONFIRMED || to == InvoiceStatus.VOIDED;
            case CONFIRMED:
                return to == InvoiceStatus.PAID || to == InvoiceStatus.VOIDED;
            case PAID:
            case VOIDED:
                return false;
            default:
                return false;
        }
    }
}
