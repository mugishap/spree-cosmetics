package com.spreecosmetics.api.v1.controllers;

import com.spreecosmetics.api.v1.dtos.CreateProductDTO;
import com.spreecosmetics.api.v1.models.Product;
import com.spreecosmetics.api.v1.payload.ApiResponse;
import com.spreecosmetics.api.v1.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody CreateProductDTO dto, @RequestParam("coverImage") MultipartFile coverImage) {
        Product product = this.productService.createProduct(dto, coverImage);
        if (product.equals(null))
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Product not created"));
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/product/create").toString());
        return ResponseEntity.created(uri).body(new ApiResponse(true, "Product created successfully", product));
    }

}
