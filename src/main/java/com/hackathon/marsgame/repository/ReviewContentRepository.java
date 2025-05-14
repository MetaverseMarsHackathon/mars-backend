package com.hackathon.marsgame.repository;

import com.hackathon.marsgame.entity.GameSession;
import com.hackathon.marsgame.entity.ReviewContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewContentRepository extends JpaRepository<ReviewContent, Long> {
    Optional<ReviewContent> findByGameSession(GameSession gameSession);
    void deleteByGameSession(GameSession gameSession);
}
