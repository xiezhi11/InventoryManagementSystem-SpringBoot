package com.example.service;

import com.example.entity.Invoice;
import com.example.entity.InvoiceStatus;
import com.example.exception.InvalidInvoiceStatusTransitionException;
import com.example.exception.InvoiceNotFoundException;
import com.example.exception.InvoiceUpdateNotAllowedException;
import com.example.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice invoiceWith(int id, InvoiceStatus status, double total, String productName) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(id);
        invoice.setStatus(status);
        invoice.setTotal(total);
        invoice.setLineTotal(total);
        invoice.setQuantity(1);
        invoice.setProductId(10);
        invoice.setProductName(productName);
        return invoice;
    }

    @Test
    void insertDefaultsToDraftAndStampsCreated() {
        Invoice invoice = new Invoice();

        invoiceService.insert(invoice);

        assertEquals(InvoiceStatus.DRAFT, invoice.getStatus());
        assertNotNull(invoice.getCreatedDateTime());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    void updateRejectsCoreFieldChangeOnPaidInvoice() {
        Invoice existing = invoiceWith(1, InvoiceStatus.PAID, 100.0, "Widget");
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(existing));
        Invoice incoming = invoiceWith(1, null, 999.0, "Widget");

        assertThrows(InvoiceUpdateNotAllowedException.class,
                () -> invoiceService.updateInvoice(incoming));
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void updateAllowsNonCoreChangeOnPaidInvoice() {
        Invoice existing = invoiceWith(1, InvoiceStatus.PAID, 100.0, "Widget");
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(existing));
        Invoice incoming = invoiceWith(1, null, 100.0, "Widget");
        incoming.setVersion(new BigDecimal("2"));

        invoiceService.updateInvoice(incoming);

        assertEquals(InvoiceStatus.PAID, existing.getStatus());
        verify(invoiceRepository).save(existing);
    }

    @Test
    void updateAllowsCoreChangeOnDraftInvoiceAndPreservesStatus() {
        Invoice existing = invoiceWith(1, InvoiceStatus.DRAFT, 100.0, "Widget");
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(existing));
        Invoice incoming = invoiceWith(1, null, 250.0, "Gadget");

        invoiceService.updateInvoice(incoming);

        assertEquals(250.0, existing.getTotal());
        assertEquals("Gadget", existing.getProductName());
        assertEquals(InvoiceStatus.DRAFT, existing.getStatus());
        verify(invoiceRepository).save(existing);
    }

    @Test
    void updateStatusValidTransitionStampsTimestamp() {
        Invoice existing = invoiceWith(1, InvoiceStatus.CONFIRMED, 100.0, "Widget");
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(existing));
        when(invoiceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Invoice result = invoiceService.updateStatus(1, InvoiceStatus.PAID);

        assertEquals(InvoiceStatus.PAID, result.getStatus());
        assertNotNull(result.getPaidDateTime());
    }

    @Test
    void updateStatusIllegalTransitionThrows() {
        Invoice existing = invoiceWith(1, InvoiceStatus.DRAFT, 100.0, "Widget");
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(existing));

        assertThrows(InvalidInvoiceStatusTransitionException.class,
                () -> invoiceService.updateStatus(1, InvoiceStatus.PAID));
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void updateStatusMissingInvoiceThrowsNotFound() {
        when(invoiceRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class,
                () -> invoiceService.updateStatus(99, InvoiceStatus.CONFIRMED));
    }
}
