package com.spreecosmetics.api.v1.controllers;

import com.spreecosmetics.api.v1.dtos.CreateInvoiceDTO;
import com.spreecosmetics.api.v1.models.Invoice;
import com.spreecosmetics.api.v1.payload.ApiResponse;
import com.spreecosmetics.api.v1.services.IInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final IInvoiceService invoiceService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createInvoice(@RequestBody @Valid CreateInvoiceDTO dto) {
        Invoice invoice = this.invoiceService.createInvoice(dto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/invoice/create").toString());
        return ResponseEntity.created(uri).body(new ApiResponse(true, "Invoice created successfully", invoice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getInvoiceById(@PathVariable(name = "id") UUID id) {
        Invoice invoice = this.invoiceService.findInvoiceById(id);
        return ResponseEntity.ok().body(new ApiResponse(true, "Invoice fetched successfully", invoice));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse> editInvoice(@RequestBody CreateInvoiceDTO dto, @PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok().body(new ApiResponse(true, "Invoice updated successfully", this.invoiceService.editInvoice(dto, id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getInvoicesByUserId(@PathVariable(name = "userId") UUID userId) {
        return ResponseEntity.ok().body(new ApiResponse(true, "Invoices fetched successfully", this.invoiceService.getInvoiceByUser(userId)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteInvoice(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok().body(new ApiResponse(true, this.invoiceService.deleteInvoice(id)));
    }



}
