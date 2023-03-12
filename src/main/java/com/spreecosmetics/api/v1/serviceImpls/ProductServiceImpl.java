package com.spreecosmetics.api.v1.serviceImpls;

import com.spreecosmetics.api.v1.dtos.CreateProductDTO;
import com.spreecosmetics.api.v1.fileHandling.File;
import com.spreecosmetics.api.v1.models.Product;
import com.spreecosmetics.api.v1.repositories.IProductRepository;
import com.spreecosmetics.api.v1.services.IFileService;
import com.spreecosmetics.api.v1.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    @Value("${uploads.directory.user_profiles}")
    private String directory;

    private final IProductRepository productRepository;
    private final IFileService fileService;

    @Override
    public Product createProduct(CreateProductDTO dto,File file) {
        Product product = new Product(dto.getName(), dto.getCurrency(), dto.getPrice(), dto.getManufacturer(), dto.getManufacturedAt(), dto.getExpiresAt());
        product.setCoverImage(file);
        return this.productRepository.save(product);
    }

    @Override
    public String deleteProduct(UUID id) {
        this.productRepository.deleteById(id);
        return "Product deleted successfully";
    }

    @Override
    public Product editProduct(UUID id, CreateProductDTO dto, File file) throws Exception {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new Exception("Product not found"));
        if (file != null) {
            product.setCoverImage(file);
        }
        product.setName(dto.getName());
        product.setCurrency(dto.getCurrency());
        product.setPrice(dto.getPrice());
        product.setManufacturer(dto.getManufacturer());
        product.setManufacturedAt(dto.getManufacturedAt());
        product.setExpiresAt(dto.getExpiresAt());
        return this.productRepository.save(product);
    }

    @Override
    public List<Product> addMultipleProducts(List<CreateProductDTO> productDTOS) {
        List<Product> products = new ArrayList<>();
        for (CreateProductDTO dto : productDTOS) {
            Product product = new Product(dto.getName(), dto.getCurrency(), dto.getPrice(), dto.getManufacturer(), dto.getManufacturedAt(), dto.getExpiresAt());
            products.add(this.productRepository.save(product));
        }
        return products;
    }

    @Override
    public String deleteMultipleProducts(List<UUID> productIds) {
        for (UUID id : productIds) {
            this.productRepository.deleteById(id);
        }
        return "Products Deleted successfully";
    }

    @Override
    public String uploadCoverImage(UUID id, File file) {
        return null;
    }
}
