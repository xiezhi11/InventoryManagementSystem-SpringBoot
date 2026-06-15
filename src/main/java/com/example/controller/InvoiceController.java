package com.example.controller;


import com.example.dto.InvoiceStatusRequest;
import com.example.entity.Invoice;
import com.example.entity.InvoiceStatus;
import com.example.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping("")
    public Iterable<Invoice> getAllInvoice(@RequestParam(required = false) InvoiceStatus status) {
        if (status != null) {
            return invoiceService.findByStatus(status);
        }
        return invoiceService.findAll();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<Invoice> searchInvoice(@PathVariable int id) {
        Optional<Invoice> invoice = invoiceService.findById(id);
        return invoice.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<Void> addInvoice(@RequestBody Invoice invoice) {
        invoiceService.insert(invoice);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Void> updateInvoice(@PathVariable int id, @RequestBody Invoice invoice) {
        invoiceService.updateInvoice(id, invoice);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}/status")
    public ResponseEntity<Void> updateInvoiceStatus(@PathVariable int id, @RequestBody InvoiceStatusRequest request) {
        invoiceService.updateInvoiceStatus(id, request);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable int id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }

}
