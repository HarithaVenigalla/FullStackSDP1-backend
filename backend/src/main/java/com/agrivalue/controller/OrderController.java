package com.agrivalue.controller;

import com.agrivalue.dto.OrderRequest;
import com.agrivalue.dto.OrderResponse;
import com.agrivalue.model.User;
import com.agrivalue.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal User buyer
    ) {
        return ResponseEntity.ok(orderService.placeOrder(request, buyer));
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<OrderResponse>> placeOrders(
            @RequestBody List<OrderRequest> requests,
            @AuthenticationPrincipal User buyer
    ) {
        return ResponseEntity.ok(orderService.placeOrders(requests, buyer));
    }

    @GetMapping("/buyer")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<OrderResponse>> getMyOrders(@AuthenticationPrincipal User buyer) {
        return ResponseEntity.ok(orderService.getBuyerOrders(buyer));
    }

    @GetMapping("/farmer")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<List<OrderResponse>> getFarmerOrders(@AuthenticationPrincipal User farmer) {
        return ResponseEntity.ok(orderService.getFarmerOrders(farmer));
    }
}
