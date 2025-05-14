package com.hackathon.marsgame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameCompletionResponse {
    private Integer score;            // 최종 점수
    private Integer totalQuestions;   // 총 문제 수
    private Integer correctAnswers;   // 맞은 문제 수
    private Integer completionTime;   // 게임 완료 시간(초)
}
