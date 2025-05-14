package com.hackathon.marsgame.controller;

import com.hackathon.marsgame.dto.AIReviewRequest;
import com.hackathon.marsgame.dto.AIReviewResponse;
import com.hackathon.marsgame.service.AIClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    
    private final AIClientService aiClientService;
    
    @GetMapping("/ai-connection")
    public ResponseEntity<String> testAIConnection() {
        try {
            // 간단한 테스트 요청 생성
            AIReviewRequest testRequest = new AIReviewRequest();
            testRequest.setIncorrectQuestions(List.of("화성은 태양에서 네 번째 행성이다."));
            
            // AI 서버 호출
            AIReviewResponse response = aiClientService.generateReview(testRequest);
            
            return ResponseEntity.ok("AI 서버 연결 성공! 응답: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("AI 서버 연결 실패: " + e.getMessage());
        }
    }
    
    @PostMapping("/ai-review")
    public ResponseEntity<AIReviewResponse> testAIReview(
            @RequestBody AIReviewRequest request) {
        try {
            // AI 서버에 복습 컨텐츠 요청
            AIReviewResponse response = aiClientService.generateReview(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 처리
            return ResponseEntity.status(500).body(null);
        }
    }
}
