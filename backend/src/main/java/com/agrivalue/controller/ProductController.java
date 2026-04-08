package com.agrivalue.controller;

import com.agrivalue.dto.ProductRequest;
import com.agrivalue.dto.ProductResponse;
import com.agrivalue.model.User;
import com.agrivalue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request,
            @AuthenticationPrincipal User farmer
    ) {
        return ResponseEntity.ok(productService.createProduct(request, farmer));
    }

    @GetMapping("/public")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/farmer")
    public ResponseEntity<List<ProductResponse>> getMyProducts(
            @AuthenticationPrincipal User farmer
    ) {
        return ResponseEntity.ok(productService.getFarmerProducts(farmer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request,
            @AuthenticationPrincipal User farmer
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, request, farmer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal User farmer
    ) {
        productService.deleteProduct(id, farmer);
        return ResponseEntity.noContent().build();
    }
}
