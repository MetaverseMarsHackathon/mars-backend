package com.hackathon.marsgame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_GAME_SESSION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    
    @Column(name = "END_TIME")
    private LocalDateTime endTime;
    
    @Column(name = "COMPLETION_TIME")
    private Integer completionTime; // 게임 완료 시간(초)
    
    @Column(name = "SCORE")
    private Integer score;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private SessionStatus status;
    
    public enum SessionStatus {
        ACTIVE, COMPLETED, ABANDONED
    }
}
