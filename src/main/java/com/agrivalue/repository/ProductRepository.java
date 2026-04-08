package com.agrivalue.repository;

import com.agrivalue.model.Product;
import com.agrivalue.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByFarmerAndActiveTrue(User farmer);
    List<Product> findByCategoryAndActiveTrue(String category);
}
