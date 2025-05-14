package com.hackathon.marsgame.client;

import com.hackathon.marsgame.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AIServiceClient {

    private final RestTemplate restTemplate;
    private final String aiServerUrl;

    public AIServiceClient(@Value("${ai.server.url}") String aiServerUrl) {
        this.restTemplate = new RestTemplate();
        this.aiServerUrl = aiServerUrl;
    }

    public AIReviewResponse generateReview(AIReviewRequest request) {
        try {
            return restTemplate.postForObject(
                    aiServerUrl + "/generate-review",
                    request,
                    AIReviewResponse.class
            );
        } catch (Exception e) {
            return createFallbackResponse(request.getIncorrectQuestions());
        }
    }

    // Fallback에서도 ExplanationItem 리스트 생성
    private AIReviewResponse createFallbackResponse(List<String> incorrectQuestions) {
        List<ExplanationItem> fallbackResults = incorrectQuestions.stream()
                .map(q -> ExplanationItem.builder()
                        .explanation("화성에 관한 질문 '" + q + "'에 대한 설명: 화성은 태양계에서 네 번째 행성으로, 지구와 가장 유사한 특성을 지닌 행성 중 하나입니다.")
                        .build())
                .collect(Collectors.toList());

        if (fallbackResults.isEmpty()) {
            fallbackResults = Collections.singletonList(
                    ExplanationItem.builder()
                            .explanation("모든 문제를 정확하게 맞혔습니다! 축하합니다!")
                            .build()
            );
        }

        return AIReviewResponse.builder()
                .results(fallbackResults)
                .build();
    }
}
