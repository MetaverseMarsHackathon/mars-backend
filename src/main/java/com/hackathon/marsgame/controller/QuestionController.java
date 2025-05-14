package com.hackathon.marsgame.controller;

import com.hackathon.marsgame.dto.QuestionResponseRequest;
import com.hackathon.marsgame.dto.QuestionResponseResponse;
import com.hackathon.marsgame.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    
    private final QuestionService questionService;
    
    @PostMapping("/{sessionId}/response")
    public ResponseEntity<QuestionResponseResponse> saveResponse(
            @PathVariable Long sessionId,
            @RequestBody QuestionResponseRequest request) {
        
        return ResponseEntity.ok(questionService.saveResponse(sessionId, request));
    }
}
