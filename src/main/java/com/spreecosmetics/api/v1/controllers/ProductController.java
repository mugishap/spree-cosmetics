package com.spreecosmetics.api.v1.controllers;

import com.spreecosmetics.api.v1.dtos.CreateProductDTO;
import com.spreecosmetics.api.v1.fileHandling.File;
import com.spreecosmetics.api.v1.models.Product;
import com.spreecosmetics.api.v1.payload.ApiResponse;
import com.spreecosmetics.api.v1.services.IFileService;
import com.spreecosmetics.api.v1.services.IProductService;
import com.spreecosmetics.api.v1.utils.Constants;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;
    private final IFileService fileService;

    @Value("${uploads.directory.user_profiles}")
    private String directory;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(
//            @RequestParam("file") MultipartFile document,
            @RequestBody CreateProductDTO dto
    ) {
//        File file = this.fileService.create(document, directory);
        Product product = this.productService.createProduct(dto, null);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/product/create").toString());
        return ResponseEntity.created(uri).body(new ApiResponse(true, "Product created successfully", product));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        return ResponseEntity.ok().body(new ApiResponse(true, "Products fetched successfully", this.productService.getAllProducts()));
    }

    @GetMapping("/paginated/all")
    public ResponseEntity<ApiResponse> getAllProductsPaginated(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, limit, Sort.Direction.ASC, "id");
        return ResponseEntity.ok().body(new ApiResponse(true, "Products fetched successfully", this.productService.getAllProductsPaginated(pageable)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable(name = "id") UUID id, @RequestBody CreateProductDTO dto, @RequestParam(name = "file", required = false) MultipartFile coverImage) throws Exception {
        if (coverImage != null && !coverImage.isEmpty()) {
            File file = this.fileService.create(coverImage, directory);
            Product product = this.productService.editProduct(id, dto, file);
            return ResponseEntity.ok().body(new ApiResponse(true, "Product updated successfully", product));
        } else {
            Product product = this.productService.editProduct(id, dto, null);
            return ResponseEntity.ok().body(new ApiResponse(true, "Product updated successfully", product));
        }
    }

}
