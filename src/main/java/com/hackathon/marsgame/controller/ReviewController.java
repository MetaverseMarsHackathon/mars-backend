package com.hackathon.marsgame.controller;

import com.hackathon.marsgame.dto.ReviewContentResponse;
import com.hackathon.marsgame.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @GetMapping("/{sessionId}/review")
    public ResponseEntity<ReviewContentResponse> getReviewContent(
            @PathVariable Long sessionId) {
        
        return ResponseEntity.ok(reviewService.getReviewContent(sessionId));
    }
}
