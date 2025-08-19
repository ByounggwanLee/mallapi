# Mall API

Spring Boot 기반의 RESTful API 서비스입니다. Todo, Product, Member 관리 기능과 JWT/OAuth2 인증을 제공합니다.

## 🚀 주요 기능

- **회원 관리**: OAuth2 소셜 로그인 (Google, GitHub) 및 JWT 인증
- **할일 관리**: 개인 Todo 목록 CRUD 작업
- **상품 관리**: 상품 등록, 조회, 수정, 삭제 및 재고 관리
- **역할 기반 권한 관리**: 사용자, 관리자, 판매자 등 역할별 접근 제어

## 🛠 기술 스택

- **Framework**: Spring Boot 3.4.4
- **Java**: OpenJDK 17
- **Build Tool**: Gradle 8.10.2
- **Database**: PostgreSQL (운영), H2 (개발/테스트)
- **ORM**: Spring Data JPA, MyBatis
- **Security**: Spring Security, JWT, OAuth2
- **Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **HTTP Client**: OpenFeign
- **Testing**: JUnit 5, Mockito, Testcontainers
- **Code Generation**: Lombok, MapStruct

## 📋 요구사항

- Java 17 이상
- Gradle 8.x
- PostgreSQL 13 이상 (운영 환경)

## 🚀 실행 방법

### 로컬 환경에서 실행

1. **프로젝트 클론**
   ```bash
   git clone https://github.com/your-repo/mall-api.git
   cd mall-api
   ```

2. **의존성 설치 및 빌드**
   ```bash
   ./gradlew build
   ```

3. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

4. **API 문서 확인**
   - Swagger UI: http://localhost:8080/api/swagger-ui.html
   - H2 Console: http://localhost:8080/api/h2-console

### 환경 변수 설정

운영 환경에서는 다음 환경 변수들을 설정해야 합니다:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=mallapi
DB_USERNAME=mallapi_user
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# OAuth2
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

## 📚 API 문서

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/api-docs

## 🏗 프로젝트 구조

```
src/
├── main/
│   ├── java/com/skax/core/
│   │   ├── MallApiApplication.java          # 메인 애플리케이션 클래스
│   │   ├── controller/                      # REST 컨트롤러
│   │   ├── service/                         # 비즈니스 로직 서비스
│   │   ├── repository/                      # 데이터 접근 계층
│   │   ├── entity/                          # JPA 엔티티
│   │   ├── dto/                             # 데이터 전송 객체
│   │   ├── client/                          # 외부 API 클라이언트
│   │   ├── config/                          # 설정 클래스
│   │   └── common/                          # 공통 유틸리티
│   │       ├── constant/                    # 상수 정의
│   │       ├── exception/                   # 예외 처리
│   │       ├── response/                    # 공통 응답 클래스
│   │       ├── security/                    # 보안 관련
│   │       └── util/                        # 유틸리티 클래스
│   └── resources/
│       ├── application.yml                  # 애플리케이션 설정
│       └── mapper/                          # MyBatis 매퍼 XML
└── test/                                    # 테스트 코드
```

## 🧪 테스트

```bash
# 모든 테스트 실행
./gradlew test

# 테스트 커버리지 리포트 생성
./gradlew jacocoTestReport

# 커버리지 검증
./gradlew jacocoTestCoverageVerification
```

## 📊 모니터링

애플리케이션 상태 확인을 위한 Actuator 엔드포인트:

- **Health Check**: http://localhost:8080/api/actuator/health
- **Application Info**: http://localhost:8080/api/actuator/info
- **Metrics**: http://localhost:8080/api/actuator/metrics

## 🔒 보안

- JWT 기반 인증/인가
- OAuth2 소셜 로그인 (Google, GitHub)
- 역할 기반 접근 제어 (RBAC)
- CORS 설정
- 요청 유효성 검증

## 📝 라이센스

이 프로젝트는 MIT 라이센스 하에 있습니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참고하세요.

## 👨‍💻 개발자

- **이병관** - [byounggwan](https://github.com/byounggwan)

## 🤝 기여

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
