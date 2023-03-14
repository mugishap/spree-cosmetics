package com.spreecosmetics.api.v1.serviceImpls;

import com.spreecosmetics.api.v1.dtos.CreateInvoiceDTO;
import com.spreecosmetics.api.v1.exceptions.ResourceNotFoundException;
import com.spreecosmetics.api.v1.models.Invoice;
import com.spreecosmetics.api.v1.models.Product;
import com.spreecosmetics.api.v1.repositories.IInvoiceRepository;
import com.spreecosmetics.api.v1.services.IInvoiceService;
import com.spreecosmetics.api.v1.services.IProductService;
import com.spreecosmetics.api.v1.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

    private final IProductService productService;
    private final IUserService userService;
    private final IInvoiceRepository invoiceRepository;

    @Override
    public Invoice createInvoice(CreateInvoiceDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setCurrency(dto.getCurrency());
        List<Product> products = dto.getProductIds().stream()
                .map(productService::findById)
                .collect(Collectors.toList());
        this.productService.reduceAmounts(products.stream().map(Product::getId).collect(Collectors.toList()));
        invoice.setProducts(products);
        invoice.setBuyer(userService.getLoggedInUser());
        invoice.setBuyerName(dto.getBuyerName());
        invoice.setPaymentMethod(dto.getPaymentMethod());
        invoice.setPurchaseLocation(dto.getPurchaseLocation());
        invoice.setTotalPrice(dto.getTotalPrice());
        this.invoiceRepository.save(invoice);
        return invoice;
    }

    @Override
    public Invoice editInvoice(CreateInvoiceDTO dto, UUID invoiceId) {
        Invoice invoice = this.invoiceRepository.findById(invoiceId).orElseThrow(() -> new ResourceNotFoundException("Invoice ", "invoice", invoiceId));
        invoice.setCurrency(dto.getCurrency());
        List<Product> products = dto.getProductIds()
                .stream()
                .map(productService::findById)
                .collect(Collectors.toList());
        invoice.setProducts(products);
        invoice.setBuyer(userService.getLoggedInUser());
        invoice.setBuyerName(dto.getBuyerName());
        invoice.setPaymentMethod(dto.getPaymentMethod());
        invoice.setPurchaseLocation(dto.getPurchaseLocation());
        invoice.setTotalPrice(dto.getTotalPrice());
        this.invoiceRepository.save(invoice);
        return null;
    }

    @Override
    public String deleteInvoice(UUID id) {
        this.invoiceRepository.deleteById(id);
        return "Invoice deleted successfully";
    }

    @Override
    public List<Invoice> getInvoiceByUser(UUID userId) {
        return this.invoiceRepository.getInvoicesByUser(this.userService.getById(userId)).orElseThrow(() -> new ResourceNotFoundException("User", "user", userId));
    }

    @Override
    public Invoice findInvoiceById(UUID id) {
        return this.invoiceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invoice", "invoice", id));
    }
}
