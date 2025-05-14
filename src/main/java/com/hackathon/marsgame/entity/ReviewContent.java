package com.hackathon.marsgame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_REVIEW_CONTENT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewContent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "SESSION_ID")
    private GameSession gameSession;
    
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "SUMMARY", columnDefinition = "TEXT")
    private String summary;
    
    @Column(name = "GENERATED_AT")
    private LocalDateTime generatedAt;
}
