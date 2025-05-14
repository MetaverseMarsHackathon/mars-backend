package com.hackathon.marsgame.service;

import com.hackathon.marsgame.dto.GameCompletionRequest;
import com.hackathon.marsgame.dto.GameCompletionResponse;
import com.hackathon.marsgame.entity.GameSession;
import com.hackathon.marsgame.entity.QuestionResponse;
import com.hackathon.marsgame.exception.ResourceNotFoundException;
import com.hackathon.marsgame.repository.GameSessionRepository;
import com.hackathon.marsgame.repository.QuestionResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    
    private final GameSessionRepository gameSessionRepository;
    private final QuestionResponseRepository questionResponseRepository;
    
    /**
     * 게임 완료 처리 및 점수 계산
     */
    @Transactional
    public GameCompletionResponse completeGame(Long sessionId, GameCompletionRequest request) {
        GameSession gameSession = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("게임 세션을 찾을 수 없습니다."));
        
        // 게임 세션 업데이트
        gameSession.setCompletionTime(request.getCompletionTime());
        gameSession.setEndTime(LocalDateTime.now());
        gameSession.setStatus(GameSession.SessionStatus.COMPLETED);
        
        // 점수 계산
        List<QuestionResponse> responses = questionResponseRepository.findByGameSession(gameSession);
        int totalQuestions = responses.size();
        long correctAnswers = responses.stream()
                .filter(QuestionResponse::getCorrect)
                .count();
        
        // 간단한 점수 계산: 정답 비율에 따라 0-100점
        int score = totalQuestions > 0 
                ? (int) ((correctAnswers * 100) / totalQuestions) 
                : 0;
        
        gameSession.setScore(score);
        gameSessionRepository.save(gameSession);
        
        return GameCompletionResponse.builder()
                .score(score)
                .totalQuestions(totalQuestions)
                .correctAnswers((int) correctAnswers)
                .completionTime(request.getCompletionTime())
                .build();
    }
}
