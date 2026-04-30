package com.agrivalue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private Long id;
    private String buyerUsername;
    private String farmerUsername;
    private String message;
    private int rating;
    private LocalDateTime createdAt;
}
