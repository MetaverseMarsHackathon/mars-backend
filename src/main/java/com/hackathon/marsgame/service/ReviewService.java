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
        
        // 모든 문제를 맞춘 경우 축하 메시지 반환 (리치텍스트 형식)
        if (incorrectQuestions.isEmpty()) {
            String richTextMessage = "<size=32><b>축하합니다!</b></size>\n\n모든 문제를 정확하게 맞혔습니다.";
            
            return ReviewContentResponse.builder()
                    .incorrectQuestions(List.of())
                    .reviewContent(new ReviewContentResponse.ReviewContent(richTextMessage))
                    .build();
        }
        
        // 기존 리뷰 컨텐츠 확인
        ReviewContent existingReview = reviewContentRepository.findByGameSession(gameSession).orElse(null);
        
        if (existingReview != null) {
            // 기존 저장된 콘텐츠 반환
            return ReviewContentResponse.builder()
                    .incorrectQuestions(incorrectQuestions)
                    .reviewContent(new ReviewContentResponse.ReviewContent(
                            existingReview.getContent()))
                    .build();
        }
        
        // AI 서버에 복습 컨텐츠 요청
        AIReviewRequest aiRequest = new AIReviewRequest(incorrectQuestions);
        AIReviewResponse aiResponse = aiClientService.generateReview(aiRequest);
        
        // AI 응답을 바로 유니티 리치텍스트 형식으로 변환
        StringBuilder richTextContent = new StringBuilder();
        richTextContent.append("<size=32><b>화성 탐사 복습 노트</b></size>\n\n");
        richTextContent.append("틀린 문제에 대한 추가 설명입니다:\n\n");
        
        // 각 설명에 번호와 리치텍스트 형식 적용
        int index = 1;
        for (ExplanationItem result : aiResponse.getResults()) {
            // 번호와 제목 형식 리치텍스트로 추가
            richTextContent.append("<size=24><b>").append(index).append(". </b></size>");
            
            // AI 응답 그대로 추가
            richTextContent.append(result.getExplanation()).append("\n\n");
            index++;
        }
        
        String finalContent = richTextContent.toString();
        
        // 리치텍스트 형식으로 DB에 저장
        ReviewContent reviewContent = ReviewContent.builder()
                .gameSession(gameSession)
                .content(finalContent)
                .generatedAt(LocalDateTime.now())
                .build();
        
        reviewContentRepository.save(reviewContent);
        
        // 리치텍스트 형식 그대로 응답 반환
        return ReviewContentResponse.builder()
                .incorrectQuestions(incorrectQuestions)
                .reviewContent(new ReviewContentResponse.ReviewContent(
                        finalContent
                ))
                .build();
    }
}
