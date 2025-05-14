package com.hackathon.marsgame.controller;

import com.hackathon.marsgame.dto.AIReviewRequest;
import com.hackathon.marsgame.dto.AIReviewResponse;
import com.hackathon.marsgame.dto.ExplanationItem;
import com.hackathon.marsgame.service.AIClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-test")
@RequiredArgsConstructor
public class ApiTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiTestController.class);
    
    private final AIClientService aiClientService;
    
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("백엔드 서버 연결 성공!");
    }
    
    @GetMapping("/ai-connection")
    public ResponseEntity<String> testAIConnection() {
        try {
            logger.info("AI 서버 연결 테스트 요청");
            
            // 간단한 테스트 요청 생성
            AIReviewRequest testRequest = new AIReviewRequest();
            testRequest.setIncorrectQuestions(List.of("화성은 태양에서 네 번째 행성이다."));
            
            // AI 서버 호출
            AIReviewResponse response = aiClientService.generateReview(testRequest);
            
            logger.info("AI 서버 응답 성공: {}", response);

            String explanations = response.getResults().stream()
                    .map(ExplanationItem::getExplanation)
                    .reduce((a, b) -> a + "\n\n" + b)
                    .orElse("설명 없음");

            return ResponseEntity.ok("AI 서버 연결 성공!\n\n응답 데이터:\n" +
                    "설명:\n" + explanations);

        } catch (Exception e) {
            logger.error("AI 서버 연결 테스트 실패", e);
            return ResponseEntity.status(500)
                    .body("AI 서버 연결 실패: " + e.getMessage() + "\n\n스택 트레이스: " + e.toString());
        }
    }
    
    @PostMapping("/ai-review")
    public ResponseEntity<AIReviewResponse> testAIReview(
            @RequestBody AIReviewRequest request) {
        try {
            logger.info("AI 서버 복습 컨텐츠 생성 테스트 요청: {}", request);
            
            // AI 서버에 복습 컨텐츠 요청
            AIReviewResponse response = aiClientService.generateReview(request);
            
            logger.info("AI 서버 복습 컨텐츠 응답: {}", response);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("AI 서버 복습 컨텐츠 생성 테스트 실패", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
