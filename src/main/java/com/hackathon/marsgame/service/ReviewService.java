package com.hackathon.marsgame.service;

import com.hackathon.marsgame.dto.AIReviewRequest;
import com.hackathon.marsgame.dto.AIReviewResponse;
import com.hackathon.marsgame.dto.ExplanationItem;
import com.hackathon.marsgame.dto.ReviewContentResponse;
import com.hackathon.marsgame.entity.GameSession;
import com.hackathon.marsgame.entity.QuestionResponse;
import com.hackathon.marsgame.entity.ReviewContent;
import com.hackathon.marsgame.exception.ResourceNotFoundException;
import com.hackathon.marsgame.repository.GameSessionRepository;
import com.hackathon.marsgame.repository.QuestionResponseRepository;
import com.hackathon.marsgame.repository.ReviewContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final GameSessionRepository gameSessionRepository;
    private final QuestionResponseRepository questionResponseRepository;
    private final ReviewContentRepository reviewContentRepository;
    private final AIClientService aiClientService;
    
    /**
     * 게임 세션에 대한 복습 컨텐츠 생성 및 조회
     */
    @Transactional
    public ReviewContentResponse getReviewContent(Long sessionId) {
        GameSession gameSession = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("게임 세션을 찾을 수 없습니다."));
        
        // 틀린 문제 응답 조회
        List<QuestionResponse> incorrectResponses = questionResponseRepository
                .findByGameSessionAndCorrect(gameSession, false);
        
        // 틀린 문제 텍스트 추출
        List<String> incorrectQuestions = incorrectResponses.stream()
                .map(QuestionResponse::getQuestionText)
                .collect(Collectors.toList());
        
        // 모든 문제를 맞춘 경우 축하 메시지 반환
        if (incorrectQuestions.isEmpty()) {
            return ReviewContentResponse.builder()
                    .incorrectQuestions(List.of())
                    .reviewContent(new ReviewContentResponse.ReviewContent(
                            "축하합니다! 모든 문제를 정확하게 맞혔습니다."))
                    .build();
        }
        
        // 기존 리뷰 컨텐츠 확인
        ReviewContent existingReview = reviewContentRepository.findByGameSession(gameSession).orElse(null);
        
        if (existingReview != null) {
            // 기존 리뷰 컨텐츠 반환
            return ReviewContentResponse.builder()
                    .incorrectQuestions(incorrectQuestions)
                    .reviewContent(new ReviewContentResponse.ReviewContent(
                            existingReview.getContent()))
                    .build();
        }
        
        // AI 서버에 복습 컨텐츠 요청
        AIReviewRequest aiRequest = new AIReviewRequest(incorrectQuestions);
        AIReviewResponse aiResponse = aiClientService.generateReview(aiRequest);
        
        // AI 응답에서 설명 합치기 (마크다운 형식으로 개선)
        StringBuilder formattedContent = new StringBuilder();
        formattedContent.append("# 화성 탐사 복습 노트\n\n");
        formattedContent.append("틀린 문제에 대한 추가 설명입니다:\n\n");
        
        // 각 설명에 번호와 마크다운 형식 적용
        int index = 1;
        for (ExplanationItem result : aiResponse.getResults()) {
            formattedContent.append("## ").append(index).append(". ").append(result.getExplanation()).append("\n\n");
            index++;
        }
        
        // 추가 학습 제안
        formattedContent.append("## 추가 학습 제안\n\n");
        formattedContent.append("화성에 대해 더 알아보고 싶다면 다음 주제들을 찾아보세요:\n\n");
        formattedContent.append("- 화성의 지질학적 특성\n");
        formattedContent.append("- 화성 탐사 역사\n");
        formattedContent.append("- 화성 생명체 탐사 계획\n");
        
        String combinedExplanations = formattedContent.toString();
        
        // 복습 컨텐츠 저장
        ReviewContent reviewContent = ReviewContent.builder()
                .gameSession(gameSession)
                .content(combinedExplanations)
                .generatedAt(LocalDateTime.now())
                .build();
        
        reviewContentRepository.save(reviewContent);
        
        // 응답 반환
        return ReviewContentResponse.builder()
                .incorrectQuestions(incorrectQuestions)
                .reviewContent(new ReviewContentResponse.ReviewContent(
                        combinedExplanations
                ))
                .build();
    }
}
