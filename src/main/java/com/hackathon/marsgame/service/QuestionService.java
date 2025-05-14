package com.hackathon.marsgame.service;

import com.hackathon.marsgame.dto.QuestionResponseRequest;
import com.hackathon.marsgame.dto.QuestionResponseResponse;
import com.hackathon.marsgame.entity.GameSession;
import com.hackathon.marsgame.entity.QuestionResponse;
import com.hackathon.marsgame.exception.ResourceNotFoundException;
import com.hackathon.marsgame.repository.GameSessionRepository;
import com.hackathon.marsgame.repository.QuestionResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final GameSessionRepository gameSessionRepository;
    private final QuestionResponseRepository questionResponseRepository;

    /**
     * 사용자의 문제 응답 저장
     */
    @Transactional
    public QuestionResponseResponse saveResponse(Long sessionId, QuestionResponseRequest request) {
        GameSession gameSession = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("게임 세션을 찾을 수 없습니다."));

        QuestionResponse questionResponse = QuestionResponse.builder()
                .gameSession(gameSession)
                .questionNumber(request.getQuestionNumber())
                .questionText(request.getQuestionText())
                .userAnswer(request.getUserAnswer())
                .correct(request.getCorrect())
                .build();

        questionResponseRepository.save(questionResponse);

        return QuestionResponseResponse.builder()
                .status(true)
                .message("문제 응답이 성공적으로 저장되었습니다.")
                .build();
    }
}
