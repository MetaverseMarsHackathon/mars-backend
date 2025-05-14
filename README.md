# Mars Game Backend

화성 탐사 해커톤 게임 프로젝트를 위한 백엔드 서버입니다. 이 Spring Boot 애플리케이션은 인증, 게임 세션 관리, 문제 응답 처리 및 AI 서비스를 활용한 복습 컨텐츠 생성 기능을 제공합니다.

## 프로젝트 구조

이 애플리케이션은 표준 Spring Boot 아키텍처를 따릅니다:

- `controller`: REST API 컨트롤러
- `service`: 비즈니스 로직 서비스
- `repository`: 데이터 액세스 레이어
- `entity`: 데이터베이스 엔티티 모델
- `dto`: 데이터 전송 객체
- `client`: 외부 서비스 클라이언트 (AI 서버)
- `config`: 설정 클래스
- `exception`: 예외 처리

## API 엔드포인트

### 인증
- `POST /auth/login`: 새 게임 세션 생성

### 문제 응답
- `POST /question/{sessionId}/response`: 사용자의 문제 응답 저장

### 복습 컨텐츠
- `GET /game/{sessionId}/review`: 틀린 답변에 대한 복습 컨텐츠 요청

### 게임 완료
- `POST /game/{sessionId}/complete`: 게임 완료 처리 및 최종 점수 반환

## 설치 및 실행

1. Java 17 이상이 설치되어 있어야 합니다
2. 저장소를 클론합니다
3. 프로젝트 디렉토리로 이동합니다
4. `.env` 파일을 생성하고 필요한 환경 변수를 설정합니다 (`.env.example` 참조)
5. `./gradlew bootRun` 명령으로 애플리케이션을 실행합니다
6. 서버는 http://localhost:8081 (또는 지정한 포트)에서 접속 가능합니다
7. Swagger UI는 http://localhost:8081/swagger-ui.html 에서 확인할 수 있습니다

## 환경 변수

프로젝트 루트에 다음 변수들이 포함된 `.env` 파일을 생성하세요:

```
# API 서버 설정
AI_SERVER_URL=http://your-ai-server-url:port

# 데이터베이스 설정
DB_USERNAME=sa
DB_PASSWORD=

# 서버 설정
SERVER_PORT=8081

# 로깅 레벨
LOG_LEVEL=DEBUG
```

## 데이터 흐름

1. 사용자가 유니티 클라이언트를 통해 로그인합니다
2. 사용자가 유니티에서 문제를 풉니다
3. 문제 응답이 백엔드로 전송됩니다
4. 게임 완료 시 유니티는 복습 컨텐츠를 요청합니다
5. 백엔드는 틀린 답변을 수집하고 AI 서버에 설명 생성을 요청합니다
6. AI로 강화된 복습 컨텐츠가 유니티로 반환됩니다
7. 게임 완료가 기록되고 최종 점수가 반환됩니다

## 의존성

- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- H2 데이터베이스 (인메모리)
- Spring Security
- Lombok
- Spring WebFlux (AI 서비스 통신용)
- SpringDoc OpenAPI (Swagger 문서화)
- Spring dotenv (환경 변수 관리)

## 개발

### H2 콘솔로 테스트하기

- H2 콘솔은 http://localhost:8081/h2-console 에서 접속 가능합니다
- JDBC URL: `jdbc:h2:mem:mars-game-db`
- 사용자명: `sa`
- 비밀번호: (비워두기)

### Swagger 문서

API 문서는 Swagger UI를 통해 확인할 수 있습니다:
- http://localhost:8081/swagger-ui.html

## 연동

- **유니티 클라이언트**: 게임 세션 관리 및 데이터 저장을 위해 이 백엔드와 통신합니다
- **AI 서비스**: 사용자의 틀린 답변을 기반으로 지능형 복습 컨텐츠를 제공합니다