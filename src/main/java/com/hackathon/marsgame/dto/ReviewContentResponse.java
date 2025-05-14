package com.hackathon.marsgame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewContentResponse {
    private List<String> incorrectQuestions; // 틀린 문제 목록
    private ReviewContent reviewContent;     // AI가 생성한 설명

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewContent {
        private String explanations; // AI가 생성한 설명
    }
}
