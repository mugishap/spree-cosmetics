package com.spreecosmetics.api.v1.services;

import com.spreecosmetics.api.v1.dtos.CreateProductDTO;
import com.spreecosmetics.api.v1.fileHandling.File;
import com.spreecosmetics.api.v1.models.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IProductService {

    public Product createProduct(CreateProductDTO dto, File file);

    public String deleteProduct(UUID id);

    public Product editProduct(UUID id, CreateProductDTO dto, File file) throws Exception;

    public List<Product> addMultipleProducts(List<CreateProductDTO> productDTOS);

    public String deleteMultipleProducts(List<UUID> productIds);

    public String uploadCoverImage(UUID id, File file);

}
