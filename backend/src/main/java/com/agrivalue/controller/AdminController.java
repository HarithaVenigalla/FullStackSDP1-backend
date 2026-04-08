package com.agrivalue.controller;

import com.agrivalue.dto.DashboardStats;
import com.agrivalue.dto.UserResponse;
import com.agrivalue.model.Role;
import com.agrivalue.service.AdminService;
import com.agrivalue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ProductService productService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/farmers")
    public ResponseEntity<java.util.List<UserResponse>> getFarmers() {
        return ResponseEntity.ok(adminService.getUsersByRole(Role.FARMER));
    }

    @GetMapping("/buyers")
    public ResponseEntity<java.util.List<UserResponse>> getBuyers() {
        return ResponseEntity.ok(adminService.getUsersByRole(Role.BUYER));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.adminDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/products")
    public ResponseEntity<Void> deleteAllProducts() {
        productService.adminDeleteAllProducts();
        return ResponseEntity.noContent().build();
    }
}
