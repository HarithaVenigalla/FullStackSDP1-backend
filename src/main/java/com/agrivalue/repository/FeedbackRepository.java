package com.agrivalue.repository;

import com.agrivalue.model.Feedback;
import com.agrivalue.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByFarmerOrderByCreatedAtDesc(User farmer);
    List<Feedback> findByBuyerOrderByCreatedAtDesc(User buyer);
}
