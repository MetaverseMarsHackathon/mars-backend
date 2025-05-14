package com.hackathon.marsgame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseRequest {
    private Integer questionNumber;
    private String questionText; // 문제 텍스트
    private String userAnswer;   // 사용자 답변 (O/X)
    private Boolean correct;     // 정답 여부
}
