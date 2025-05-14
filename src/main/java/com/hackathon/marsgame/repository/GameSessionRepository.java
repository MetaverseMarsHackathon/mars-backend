package com.hackathon.marsgame.repository;

import com.hackathon.marsgame.entity.GameSession;
import com.hackathon.marsgame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findByUser(User user);
    Optional<GameSession> findByIdAndUser(Long id, User user);
}
