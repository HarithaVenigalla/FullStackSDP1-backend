package com.agrivalue.service;

import com.agrivalue.dto.FeedbackRequest;
import com.agrivalue.dto.FeedbackResponse;
import com.agrivalue.model.Feedback;
import com.agrivalue.model.User;
import com.agrivalue.repository.FeedbackRepository;
import com.agrivalue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Transactional
    public FeedbackResponse saveFeedback(FeedbackRequest request, User buyer) {
        User farmer = userRepository.findById(request.getFarmerId())
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Feedback feedback = Feedback.builder()
                .buyer(buyer)
                .farmer(farmer)
                .message(request.getMessage())
                .rating(request.getRating())
                .build();

        return mapToResponse(feedbackRepository.save(feedback));
    }

    public List<FeedbackResponse> getFarmerFeedback(Long farmerId) {
        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        return feedbackRepository.findByFarmerOrderByCreatedAtDesc(farmer).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .buyerUsername(feedback.getBuyer().getUsername())
                .farmerUsername(feedback.getFarmer().getUsername())
                .message(feedback.getMessage())
                .rating(feedback.getRating())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
