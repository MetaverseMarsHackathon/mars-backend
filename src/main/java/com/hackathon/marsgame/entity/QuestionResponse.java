package com.hackathon.marsgame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBL_QUESTION_RESPONSE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESPONSE_ID")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "SESSION_ID")
    private GameSession gameSession;
    
    @Column(name = "QUESTION_NUMBER")
    private Integer questionNumber;
    
    @Column(name = "QUESTION_TEXT")
    private String questionText;
    
    @Column(name = "USER_ANSWER")
    private String userAnswer;
    
    @Column(name = "IS_CORRECT")
    private Boolean correct;
}
