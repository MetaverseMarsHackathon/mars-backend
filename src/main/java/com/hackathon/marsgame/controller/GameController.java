package com.hackathon.marsgame.controller;

import com.hackathon.marsgame.dto.GameCompletionRequest;
import com.hackathon.marsgame.dto.GameCompletionResponse;
import com.hackathon.marsgame.dto.ReviewContentResponse;
import com.hackathon.marsgame.service.GameService;
import com.hackathon.marsgame.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    
    private final GameService gameService;
    private final ReviewService reviewService;
    
    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<GameCompletionResponse> completeGame(
            @PathVariable Long sessionId,
            @RequestBody GameCompletionRequest request) {
        
        return ResponseEntity.ok(gameService.completeGame(sessionId, request));
    }
    
    @GetMapping("/{sessionId}/review")
    public ResponseEntity<ReviewContentResponse> getReviewContent(
            @PathVariable Long sessionId) {
        
        return ResponseEntity.ok(reviewService.getReviewContent(sessionId));
    }
}
