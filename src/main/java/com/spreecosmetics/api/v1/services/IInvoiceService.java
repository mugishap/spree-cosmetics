package com.spreecosmetics.api.v1.services;

import com.spreecosmetics.api.v1.dtos.CreateInvoiceDTO;
import com.spreecosmetics.api.v1.models.Invoice;

import java.util.List;
import java.util.UUID;

public interface IInvoiceService {

    public Invoice createInvoice(CreateInvoiceDTO dto);

    public Invoice editInvoice(CreateInvoiceDTO dto, UUID invoiceId);

    public String deleteInvoice(UUID id);

    public List<Invoice> getInvoiceByUser(UUID userId);

    public Invoice findInvoiceById(UUID id);
}
