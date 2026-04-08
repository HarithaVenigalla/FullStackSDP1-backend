package com.agrivalue.service;

import com.agrivalue.dto.DashboardStats;
import com.agrivalue.model.Order;
import com.agrivalue.model.Product;
import com.agrivalue.repository.OrderRepository;
import com.agrivalue.repository.ProductRepository;
import com.agrivalue.dto.UserResponse;
import com.agrivalue.model.Role;
import com.agrivalue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardStats getStats() {
        var orders = orderRepository.findAll();
        var products = productRepository.findAll();
        
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> categoryDistribution = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        return DashboardStats.builder()
                .totalUsers(userRepository.count())
                .totalBuyers(userRepository.countByRole(com.agrivalue.model.Role.BUYER))
                .totalFarmers(userRepository.countByRole(com.agrivalue.model.Role.FARMER))
                .totalProducts(productRepository.count())
                .totalOrders(orderRepository.count())
                .totalRevenue(totalRevenue)
                .categoryDistribution(categoryDistribution)
                .build();
    }

    public java.util.List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == role)
                .map(u -> {
                    com.agrivalue.model.User user = (com.agrivalue.model.User) u;
                    return UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createdAt(user.getCreatedAt())
                        .build();
                })
                .collect(Collectors.toList());
    }
}
