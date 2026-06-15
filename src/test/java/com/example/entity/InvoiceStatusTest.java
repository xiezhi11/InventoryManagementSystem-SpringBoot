package com.example.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvoiceStatusTest {

    @Test
    void draftCanGoToConfirmedAndVoidOnly() {
        assertTrue(InvoiceStatus.DRAFT.canTransitionTo(InvoiceStatus.CONFIRMED));
        assertTrue(InvoiceStatus.DRAFT.canTransitionTo(InvoiceStatus.VOID));
        assertFalse(InvoiceStatus.DRAFT.canTransitionTo(InvoiceStatus.PAID));
        assertFalse(InvoiceStatus.DRAFT.canTransitionTo(InvoiceStatus.DRAFT));
        assertFalse(InvoiceStatus.DRAFT.canTransitionTo(null));
    }

    @Test
    void confirmedCanGoToPaidAndVoidOnly() {
        assertTrue(InvoiceStatus.CONFIRMED.canTransitionTo(InvoiceStatus.PAID));
        assertTrue(InvoiceStatus.CONFIRMED.canTransitionTo(InvoiceStatus.VOID));
        assertFalse(InvoiceStatus.CONFIRMED.canTransitionTo(InvoiceStatus.DRAFT));
        assertFalse(InvoiceStatus.CONFIRMED.canTransitionTo(InvoiceStatus.CONFIRMED));
    }

    @Test
    void paidIsTerminal() {
        for (InvoiceStatus target : InvoiceStatus.values()) {
            assertFalse(InvoiceStatus.PAID.canTransitionTo(target),
                    "PAID must not transition to " + target);
        }
    }

    @Test
    void voidIsTerminal() {
        for (InvoiceStatus target : InvoiceStatus.values()) {
            assertFalse(InvoiceStatus.VOID.canTransitionTo(target),
                    "VOID must not transition to " + target);
        }
    }
}
