package com.example.service;

import com.example.entity.Invoice;
import com.example.entity.InvoiceStatus;
import com.example.exception.InvalidInvoiceStatusTransitionException;
import com.example.exception.InvoiceNotFoundException;
import com.example.exception.InvoiceUpdateNotAllowedException;
import com.example.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
        if (invoice.getCreatedDateTime() == null) {
            invoice.setCreatedDateTime(new Date());
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

    /**
     * Updates an existing invoice. For PAID or VOID invoices, any change to the
     * core fields (amounts / product information) is rejected. Status and the
     * lifecycle timestamps are never modified through this path - use
     * {@link #updateStatus(int, InvoiceStatus)} for that.
     */
    public void updateInvoice(Invoice invoice) {
        Invoice existing = invoiceRepository.findById(invoice.getInvoiceId()).orElse(null);
        if (existing == null) {
            // Preserve the original upsert behaviour for unknown ids.
            insert(invoice);
            return;
        }

        InvoiceStatus status = existing.getStatus();
        boolean locked = status == InvoiceStatus.PAID || status == InvoiceStatus.VOID;
        if (locked && coreFieldsChanged(existing, invoice)) {
            throw new InvoiceUpdateNotAllowedException(
                    "Invoice " + existing.getInvoiceId() + " is " + status
                            + " and its amounts/product information can no longer be updated");
        }

        // Merge only the editable fields; status and timestamps stay as stored.
        existing.setLineTotal(invoice.getLineTotal());
        existing.setTotal(invoice.getTotal());
        existing.setProductId(invoice.getProductId());
        existing.setProductName(invoice.getProductName());
        existing.setQuantity(invoice.getQuantity());
        existing.setVersion(invoice.getVersion());
        invoiceRepository.save(existing);
    }

    /**
     * Moves an invoice to {@code target} if the transition is allowed and stamps
     * the corresponding lifecycle timestamp.
     */
    public Invoice updateStatus(int id, InvoiceStatus target) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found: " + id));

        InvoiceStatus current = invoice.getStatus() == null ? InvoiceStatus.DRAFT : invoice.getStatus();
        if (!current.canTransitionTo(target)) {
            throw new InvalidInvoiceStatusTransitionException(
                    "Cannot transition invoice " + id + " from " + current + " to " + target);
        }

        invoice.setStatus(target);
        Date now = new Date();
        switch (target) {
            case CONFIRMED:
                invoice.setConfirmedDateTime(now);
                break;
            case PAID:
                invoice.setPaidDateTime(now);
                break;
            case VOID:
                invoice.setVoidedDateTime(now);
                break;
            default:
                break;
        }
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }

    private boolean coreFieldsChanged(Invoice existing, Invoice incoming) {
        return Double.compare(existing.getLineTotal(), incoming.getLineTotal()) != 0
                || Double.compare(existing.getTotal(), incoming.getTotal()) != 0
                || Double.compare(existing.getQuantity(), incoming.getQuantity()) != 0
                || existing.getProductId() != incoming.getProductId()
                || !Objects.equals(existing.getProductName(), incoming.getProductName());
    }
}
