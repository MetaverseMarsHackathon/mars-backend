package com.hackathon.marsgame.service;

import com.hackathon.marsgame.dto.AIReviewRequest;
import com.hackathon.marsgame.dto.AIReviewResponse;
import com.hackathon.marsgame.dto.ExplanationItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIClientService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIClientService.class);
    
    private final WebClient webClient;
    private final String aiServerUrl;
    
    // AI 팀에서 제공한 새 엔드포인트 경로
    private static final String AI_ENDPOINT = "/questions";
    
    public AIClientService(@Value("${ai.server.url}") String aiServerUrl) {
        this.aiServerUrl = aiServerUrl;
        this.webClient = WebClient.builder()
                .baseUrl(aiServerUrl)
                .build();
        
        logger.info("AIClientService 초기화 - AI 서버 URL: {}", aiServerUrl);
    }
    
    /**
     * AI 서버에 복습 컨텐츠 생성 요청
     * 동기식으로 처리하기 위해 blocking 호출 사용
     */
    public AIReviewResponse generateReview(AIReviewRequest request) {
        logger.info("AI 서버에 복습 컨텐츠 요청 시작 - 문제 수: {}", request.getIncorrectQuestions().size());
        logger.info("요청 데이터: {}", request);
        
        try {
            logger.info("호출 URL: {}{}", aiServerUrl, AI_ENDPOINT);
            
            AIReviewResponse response = webClient.post()
                    .uri(AI_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AIReviewResponse.class)
                    .doOnNext(res -> logger.info("AI 서버 응답 성공: {}", res))
                    .doOnError(e -> logger.error("AI 서버 응답 오류: {}", e.getMessage()))
                    .onErrorResume(e -> {
                        logger.warn("AI 서버 오류, 폴백 응답 생성: {}", e.getMessage());
                        return createFallbackResponse(request);
                    })
                    .block(); // 동기식 처리
            
            return response;
        } catch (Exception e) {
            logger.error("AI 서버 연결 실패: {}", e.getMessage(), e);
            // 예외 발생 시 폴백 응답 반환
            return createFallbackResponse(request).block();
        }
    }
    
    /**
     * AI 서버 연결 실패 시 기본 응답 생성
     */
    private Mono<AIReviewResponse> createFallbackResponse(AIReviewRequest request) {
        List<ExplanationItem> fallbackResults = request.getIncorrectQuestions().stream()
                .map(q -> ExplanationItem.builder()
                        .explanation("화성에 관한 문제 '" + q + "'에 대한 기본 설명입니다. " +
                                "화성은 태양계에서 네 번째 행성으로, 붉은 색을 띠고 있습니다. " +
                                "이는 표면의 산화철(녹) 때문입니다. 화성에는 포보스와 데이모스라는 두 개의 위성이 있습니다.")
                        .build())
                .collect(Collectors.toList());

        if (fallbackResults.isEmpty()) {
            fallbackResults = Collections.singletonList(
                    ExplanationItem.builder()
                            .explanation("모든 문제를 정확하게 맞혔습니다! 축하합니다!")
                            .build()
            );
        }

        AIReviewResponse response = AIReviewResponse.builder()
                .results(fallbackResults)
                .build();

        return Mono.just(response);
    }
}
