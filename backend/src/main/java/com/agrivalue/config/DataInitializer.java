package com.agrivalue.config;

import com.agrivalue.model.Product;
import com.agrivalue.model.Role;
import com.agrivalue.model.User;
import com.agrivalue.repository.ProductRepository;
import com.agrivalue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Create Admin
            var admin = User.builder()
                    .username("admin")
                    .email("admin@agrivalue.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);

            // Create Farmer
            var farmer = User.builder()
                    .username("farmer_john")
                    .email("john@farm.com")
                    .password(passwordEncoder.encode("farmer123"))
                    .role(Role.FARMER)
                    .build();
            userRepository.save(farmer);

            // Create Buyer
            var buyer = User.builder()
                    .username("buyer_jane")
                    .email("jane@market.com")
                    .password(passwordEncoder.encode("buyer123"))
                    .role(Role.BUYER)
                    .build();
            userRepository.save(buyer);

            // Create Initial Products
            var p1 = Product.builder()
                    .name("Organic Golden Corn")
                    .description("Freshly harvested sweet golden corn from the heart of the valley.")
                    .category("Grains")
                    .price(new BigDecimal("12.50"))
                    .stock(150)
                    .imageUrl("https://images.unsplash.com/photo-1551754655-cd27e38d2076?w=400&h=300&fit=crop")
                    .farmer(farmer)
                    .build();

            var p2 = Product.builder()
                    .name("Premium Vine Tomatoes")
                    .description("Plump, juicy red tomatoes grown in climate-controlled greenhouses.")
                    .category("Vegetables")
                    .price(new BigDecimal("4.99"))
                    .stock(80)
                    .imageUrl("https://images.unsplash.com/photo-1592924357228-91a4daadcfea?w=400&h=300&fit=crop")
                    .farmer(farmer)
                    .build();

            productRepository.saveAll(List.of(p1, p2));
            System.out.println("Data Seeding Complete: Admin, Farmer, Buyer, and Products created.");
        }

        System.out.println("--------------------------------------------------");
        System.out.println("CURRENT USERS IN DATABASE (SQL WORKBENCH VIEW):");
        userRepository.findAll().forEach(u -> {
            if (u.getCreatedAt() == null) {
                u.setCreatedAt(java.time.LocalDateTime.now());
                userRepository.save(u);
            }
            System.out.printf("ID: %d | Username: %s | Email: %s | Role: %s | Join Date: %s%n", 
                u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getCreatedAt());
        });
        System.out.println("--------------------------------------------------");
    }
}
