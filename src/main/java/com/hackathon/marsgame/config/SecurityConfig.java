package com.hackathon.marsgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // 개발용: 인코딩하지 않음
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 해커톤 용도로 CSRF 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            // 세션 관리 설정
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()       // 인증 관련 경로 허용
                .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 접근 허용
                .requestMatchers("/question/**").permitAll()   // 게임 중 문제 응답 허용
                .requestMatchers("/game/**").permitAll()       // 게임 관련 기능 허용
                .requestMatchers("/test/**").permitAll()       // 테스트 엔드포인트 허용
                .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI 허용
                .requestMatchers("/swagger-ui.html").permitAll() // Swagger 진입점 허용
                .requestMatchers("/api-docs/**").permitAll()   // API Docs 허용
                .requestMatchers("/ai-test.html").permitAll()  // AI 테스트 페이지 허용
                .requestMatchers("/*.html").permitAll()        // 정적 HTML 파일 허용
                .requestMatchers("/*.js").permitAll()          // 정적 JS 파일 허용
                .requestMatchers("/*.css").permitAll()         // 정적 CSS 파일 허용
                .anyRequest().permitAll())                     // 해커톤 용도로 모든 요청 허용
            // H2 콘솔 사용을 위한 설정
            .headers(headers -> headers.frameOptions().disable());
        
        return http.build();
    }
}
