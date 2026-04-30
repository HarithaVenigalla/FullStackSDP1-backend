package com.agrivalue.controller;

import com.agrivalue.dto.FeedbackRequest;
import com.agrivalue.dto.FeedbackResponse;
import com.agrivalue.model.User;
import com.agrivalue.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @RequestBody FeedbackRequest request,
            @AuthenticationPrincipal User buyer
    ) {
        return ResponseEntity.ok(feedbackService.saveFeedback(request, buyer));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<FeedbackResponse>> getFarmerFeedback(@PathVariable Long farmerId) {
        return ResponseEntity.ok(feedbackService.getFarmerFeedback(farmerId));
    }
}
