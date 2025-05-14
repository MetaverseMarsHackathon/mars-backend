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
public class AIReviewRequest {
    // JSON 요청 형식에 맞게 필드명 수정
    private List<String> incorrectQuestions;
}
