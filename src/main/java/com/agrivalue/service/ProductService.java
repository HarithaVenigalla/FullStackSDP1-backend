package com.agrivalue.service;

import com.agrivalue.dto.ProductRequest;
import com.agrivalue.dto.ProductResponse;
import com.agrivalue.model.Product;
import com.agrivalue.model.User;
import com.agrivalue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductRequest request, User farmer) {
        var product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .farmer(farmer)
                .build();
        return mapToResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProductResponse> getFarmerProducts(User farmer) {
        // More robust filtering
        return productRepository.findByFarmerAndActiveTrue(farmer).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        return mapToResponse(productRepository.findById(id)
                .filter(p -> p.isActive())
                .orElseThrow(() -> new RuntimeException("Product not found or inactive")));
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .farmerUsername(product.getFarmer().getUsername())
                .farmerId(product.getFarmer().getId())
                .build();
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request, User farmer) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (!product.getFarmer().getId().equals(farmer.getId())) {
            throw new RuntimeException("Permission Denied: You do not own this product.");
        }
        
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        
        return mapToResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id, User farmer) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Use numeric ID comparison for better reliability
        if (!product.getFarmer().getId().equals(farmer.getId())) {
            throw new RuntimeException("Permission Denied: You do not own this product.");
        }
        
        product.setActive(false);
        productRepository.save(product);
    }

    @Transactional
    public void adminDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    @Transactional
    public void adminDeleteAllProducts() {
        List<Product> products = productRepository.findByActiveTrue();
        products.forEach(p -> p.setActive(false));
        productRepository.saveAll(products);
    }
}
