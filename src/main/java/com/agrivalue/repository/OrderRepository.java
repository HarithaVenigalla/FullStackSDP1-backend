package com.agrivalue.repository;

import com.agrivalue.model.Order;
import com.agrivalue.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(User buyer);
    List<Order> findByProduct_Farmer(User farmer);
}
