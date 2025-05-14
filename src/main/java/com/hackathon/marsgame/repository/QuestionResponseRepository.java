package com.hackathon.marsgame.repository;

import com.hackathon.marsgame.entity.GameSession;
import com.hackathon.marsgame.entity.QuestionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {
    List<QuestionResponse> findByGameSession(GameSession gameSession);
    List<QuestionResponse> findByGameSessionAndCorrect(GameSession gameSession, Boolean correct);
    int countByGameSessionAndCorrect(GameSession gameSession, Boolean correct);
}
