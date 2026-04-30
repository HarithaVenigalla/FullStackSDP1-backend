package com.agrivalue.service;

import com.agrivalue.dto.OrderRequest;
import com.agrivalue.dto.OrderResponse;
import com.agrivalue.model.Order;
import com.agrivalue.model.User;
import com.agrivalue.repository.OrderRepository;
import com.agrivalue.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<OrderResponse> placeOrders(List<OrderRequest> requests, User buyer) {
        return requests.stream()
                .map(request -> placeOrder(request, buyer))
                .toList();
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest request, User buyer) {
        var product = productRepository.findById(request.getProductId()).orElseThrow();
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        var order = Order.builder()
                .buyer(buyer)
                .product(product)
                .quantity(request.getQuantity())
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .status("COMPLETED")
                .paymentType(request.getPaymentType())
                .build();
        
        return mapToResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getBuyerOrders(User buyer) {
        return orderRepository.findByBuyer(buyer).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<OrderResponse> getFarmerOrders(User farmer) {
        return orderRepository.findByProduct_Farmer(farmer).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProduct().getId())
                .productName(order.getProduct().getName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentType(order.getPaymentType())
                .buyerUsername(order.getBuyer().getUsername())
                .farmerId(order.getProduct().getFarmer().getId())
                .farmerUsername(order.getProduct().getFarmer().getUsername())
                .orderDate(order.getOrderDate())
                .build();
    }
}
