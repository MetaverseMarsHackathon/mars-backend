package com.hackathon.marsgame.service;

import com.hackathon.marsgame.dto.LoginRequest;
import com.hackathon.marsgame.dto.LoginResponse;
import com.hackathon.marsgame.entity.GameSession;
import com.hackathon.marsgame.entity.User;
import com.hackathon.marsgame.repository.GameSessionRepository;
import com.hackathon.marsgame.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final GameSessionRepository gameSessionRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 로그인 처리 및 새 게임 세션 생성
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 사용자 검증
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        
        // 해커톤을 위해 단순화: 사용자가 없으면 자동 생성
        User user;
        if (userOptional.isEmpty()) {
            user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role("USER")
                    .build();
            userRepository.save(user);
        } else {
            user = userOptional.get();
            // 실제 애플리케이션에서는 비밀번호 검증 필요
            // if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            //     return LoginResponse.builder()
            //             .success(false)
            //             .message("비밀번호가 일치하지 않습니다.")
            //             .build();
            // }
        }

        // 새 게임 세션 생성
        GameSession gameSession = GameSession.builder()
                .user(user)
                .startTime(LocalDateTime.now())
                .status(GameSession.SessionStatus.ACTIVE)
                .build();

        gameSessionRepository.save(gameSession);

        return LoginResponse.builder()
                .sessionId(gameSession.getId())
                .username(user.getUsername())
                .success(true)
                .message("로그인 성공")
                .build();
    }
    
    /**
     * 세션 ID로 게임 세션 조회
     */
    public Optional<GameSession> getGameSession(Long sessionId) {
        return gameSessionRepository.findById(sessionId);
    }
}
