# GitHub Copilot Instructions for Spring Boot Project

## 프로젝트 정보
- **프로젝트명**: mall-api
- **개발자**: ByounggwanLee
- **생성일**: 2025-08-19
- **목표**: Spring Boot MallApi(todo,product,member,memberRole,JWT/OAuth2 Login) RESTful API 개발
- **Java 버전**: 17+
- **Spring Boot 버전**: 3.4.4
- **빌드 도구**: Gradle
- **데이터베이스**: PostgreSQL (운영), PostgreSQL (테스트), PostgreSQL (개발), H2 (로컬 개발)

## 기술 스택
- **ORM**: Spring Data JPA, MyBatis
- **External API**: Spring Cloud OpenFeign
- **Documentation**: SpringDoc OpenAPI 3
- **Testing**: JUnit 5, Mockito
- **Logging**: SLF4J, Logback, 
- **Security**: Spring Security, JWT
- **Utilities**: Lombok, Jakarta Validation, MapStruct
- **DevTools**: Spring Boot DevTools (개발 환경 전용)

## 코딩 스타일 가이드

### 1. 네이밍 컨벤션
- **클래스명**: PascalCase (예: UserService, ProductController)
- **메서드명**: camelCase (예: createUser, findByEmail)
- **변수명**: camelCase (예: userId, createdAt)
- **상수명**: UPPER_SNAKE_CASE (예: MAX_RETRY_COUNT)
- **패키지명**: lowercase with dots (예: com.byounggwanlee.project)

### 2. 파일 구조 및 패키지 규칙
```
com.skax.core/
├── client/                # Feign Client 인터페이스
├── config/                # 설정 클래스
├── controller/            # REST 컨트롤러
├── service/               # 비즈니스 로직
├── repository/            # 데이터 액세스
├── entity/                # JPA 엔티티
├── dto/                   # 데이터 전송 객체(MapStruct Mapper포함)
└── common/                # 공통 기능
    ├── constant/          # 상수 정의
    ├── exception/         # 예외 처리
    ├── response/          # 응답 관련 (AxResponse, AxResponseEntity, PageResponse)
    ├── security/          # 보안 관련
    └── util/              # 유틸리티
```
- **controller, service, entity, repository, dto, client**는 각각 최상위 디렉토리로 생성
- 각 계층 디렉토리 내부에 **도메인명(user, product, order 등)으로 하위 디렉토리**를 생성
- DTO는 도메인별 하위 디렉토리에 request/response로 세분화
- Service의 Implementation은 도메인별 하위 디렉토리에 impl에 생성
- mapstruct는 DTO는 도메인별 하위 디렉토리 생성
- client는 외부 연동별로 도메인 하위 디렉토리 생성
- 공통기능은 최상위에 common을 두고 상수, 예외, 설정, 응답, 보안, 유틸리티 등은 별도 디렉토리(constant, exception, response, security, util 등)에서 관리
- BaseEntity는 최상위 entity 디렉토리에 생성
- BaseDto는  최상위 dto 디렉토리에 생성
- BaseDto, BaseEntity등 공통 베이스 클래스를 생성하여 상속 구조를 활용

### 3. 클래스 및 파일명 규칙

- Controller: 도메인명 + Controller (ex. UserController)
- Service: 도메인명 + Service, 구현체는 ServiceImpl (ex. ProductServiceImpl)
- Entity: 도메인 단수형 (ex. User, Product)
- Repository: 도메인명 + Repository (ex. OrderRepository)
- DTO: 도메인명 + 용도 + Request/Response (ex. UserCreateRequest, ProductResponse)
- Client: 도메인명 + ExternalClient/FeignClient/ApiClient 등 (ex. PaymentExternalClient)

---

### 4. 어노테이션 사용 규칙
- **Lombok**: @Getter, @Builder, @NoArgsConstructor 우선 사용
- **JPA**: @Entity, @Table, @Column 명시적 설정
- **Validation**: @Valid, @NotNull, @NotBlank 적극 활용
- **Spring**: @Service, @Repository, @RestController 표준 사용

### 5. 메서드 구현 패턴
```java
// Service 메서드 패턴
@Transactional(readOnly = true)
public EntityResponse getEntityById(Long id) {
    Entity entity = findEntityById(id);
    return EntityResponse.from(entity);
}

private Entity findEntityById(Long id) {
    return entityRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
}
```

### 6. 공통 기능
- **RESTful API 설계**: 일관된 REST API 패턴
- **통합된 응답 포맷**: AxResponseEntity<T> 래퍼를 통한 표준화된 응답 (ResponseEntity + AxResponse 통합)
- **페이징 처리**: AxResponseEntity<PageResponse<T>>를 통한 효율적인 대용량 데이터 처리
- **예외 처리**: GlobalExceptionHandler를 통한 통합 예외 관리
- **API 문서화**: OpenAPI 3 기반 자동 문서 생성
- **입력 검증**: Jakarta Validation을 통한 요청 데이터 검증
- **로깅**: 구조화된 로깅 및 요청 추적 자동생성
- **주석**: JavaDoc과 OpenAPI 생성
- **상수화**: 애플리케이션 상수 클래스를 생성하여 사용
- **Mapping**: MapStruct를 통한 DTO와 Entity 간 변환(DTO 디렉토리에 생성)
- **HTTP 상태 코드**: 표준화된 상태 코드와 메시지 자동 설정

### 7. 필수 사항
- ** 로깅 ** : 실행모듈에 대한 로깅을 반드시 구현
- ** 문서화 ** 
  - 모든 API 엔드포인트에 대한 OpenAPI 문서를 작성
  - 모든 클래스와 메서드에 JavaDoc 주석 작성
- ** 테스트 코드 ** : JUnit 5와 Mockito를 사용한 단위 테스트 작성

### 8. 기타 규칙 및 권장사항
- 새로운 도메인 추가 시 모든 계층 디렉토리 내에 동일한 도메인 하위 디렉토리 생성
- 각 도메인별 파일은 해당 도메인과 계층 역할이 명확히 드러나도록 작성
- 테스트 코드는 동일한 구조로 `src/test/java/com/{owner}/{project}/{계층}/{도메인}/`에 생성
- 공통 기능/설정/유틸리티는 별도 디렉토리에서 관리
- 각 계층별 클래스에는 역할 및 용도를 JavaDoc/주석으로 명확히 기술

## API 설계 규칙

### 1. REST API 엔드포인트 패턴
```
GET    /api/v1/users          # 목록 조회
GET    /api/v1/users/{id}     # 단일 조회
POST   /api/v1/users          # 생성
PUT    /api/v1/users/{id}     # 전체 수정
PATCH  /api/v1/users/{id}     # 부분 수정
DELETE /api/v1/users/{id}     # 삭제
```

### 2. 응답 형식 표준화 (AxResponseEntity 사용)

#### 개요

##### 목적
- API 응답의 일관성 확보
- 클라이언트 개발 효율성 향상
- 에러 처리 표준화
- 다국어 지원 기반 마련

##### 적용 범위
- 모든 REST API 엔드포인트
- 에러 응답 처리
- 페이지네이션 응답
- 파일 업로드/다운로드 응답

#### 🏗️ 응답 구조 표준

##### 기본 응답 구조

```json
{
  "success": boolean,
  "message": "string",
  "data": object | array | null,
  "error": {
    "hscode": "string",
    "code": "string",
    "message": "string",
    "details": "string",
    "timestamp": "2025-08-08T04:01:33Z",
    "path": "/api/users/123",
    "fieldErrors": [
      {
        "field": "email",
        "rejectedValue": "invalid-email",
        "message": "올바른 이메일 형식이 아닙니다"
      }
    ]
  },
  "timestamp": "2025-08-08T04:01:33Z",
  "path": "/api/users"
}
```

##### 응답 필드 설명

| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| `success` | boolean | ✅ | 요청 성공 여부 |
| `message` | string | ✅ | 응답 메시지 |
| `data` | any | ❌ | 응답 데이터 (성공 시) |
| `error` | object | ❌ | 에러 정보 (실패 시) |
| `timestamp` | string | ✅ | 응답 생성 시간 (ISO 8601) |
| `path` | string | ✅ | 요청 경로 |


#### ✅ 성공 응답 표준
// Controller 메서드 패턴 - 단일조회
@GetMapping("/{id}")
public AxResponseEntity<UserResponse> getUser(@PathVariable Long id) {
    UserResponse user = userService.getUserById(id);
    return AxResponseEntity.ok(user, "사용자 정보를 성공적으로 조회했습니다.");
}

// Controller 메서드 패턴 - 목록조회
@GetMapping
public AxResponseEntity<PageResponse<UserResponse>> getUsers(
        @PageableDefault(size = 20) Pageable pageable) {
    Page<UserResponse> users = userService.getUsers(pageable);
    return AxResponseEntity.okPage(users, "사용자 목록을 성공적으로 조회했습니다.");
}


// Controller 메서드 패턴 - 생성
@PostMapping
public AxResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
    UserResponse user = userService.createUser(request);
    return AxResponseEntity.created(user, "사용자가 성공적으로 생성되었습니다.");
}

// Controller 메서드 패턴 - 수정
@PutMapping("/{id}")
public AxResponseEntity<UserResponse> updateUser(
        @PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
    UserResponse user = userService.updateUser(id, request);
    return AxResponseEntity.updated(user, "사용자 정보가 성공적으로 수정되었습니다.");
}

// Controller 메서드 패턴 - 삭제
@DeleteMapping("/{id}")
public AxResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return AxResponseEntity.deleted("사용자가 성공적으로 삭제되었습니다.");
}

#### ❌ 에러 응답 표준

##### 1. 리소스 없음 (404)

```json
{
  "success": false,
  "message": "사용자를 찾을 수 없습니다",
  "error": {
    "hscode": "NOT_FOUND",
    "code": "U001",
    "message": "사용자를 찾을 수 없습니다",
    "details": "ID 999에 해당하는 사용자가 존재하지 않습니다",
    "timestamp": "2025-08-15T08:07:48",
    "path": "/api/users/999"
  },
  "timestamp": "2025-08-15T08:07:48",
  "path": "/api/users/999"
}
```

##### 2. 유효성 검증 실패 (400)

```json
{
  "success": false,
  "message": "입력값 검증에 실패했습니다",
  "error": {
    "hscode": "BAD_REQUEST",
    "code": "V001",
    "message": "입력값이 올바르지 않습니다",
    "details": "필수 필드가 누락되었거나 형식이 올바르지 않습니다",
    "timestamp": "2025-08-15T08:07:48",
    "path": "/api/users",
    "fieldErrors": [
      {
        "field": "name",
        "rejectedValue": "",
        "message": "이름은 필수입니다"
      },
      {
        "field": "email",
        "rejectedValue": "invalid-email",
        "message": "올바른 이메일 형식이 아닙니다"
      },
      {
        "field": "age",
        "rejectedValue": -1,
        "message": "나이는 0 이상이어야 합니다"
      }
    ]
  },
  "timestamp": "2025-08-15T08:07:48",
  "path": "/api/users"
}
```

##### 3. 인증 실패 (401)

```json
{
  "success": false,
  "message": "인증에 실패했습니다",
  "error": {
    "hscode": "UNAUTHORIZED",
    "code": "A001",
    "message": "유효하지 않은 인증 정보입니다",
    "details": "JWT 토큰이 만료되었거나 올바르지 않습니다",
    "timestamp": "2025-08-15T08:07:48",
    "path": "/api/users/profile"
  },
  "timestamp": "2025-08-15T08:07:48",
  "path": "/api/users/profile"
}
```

##### 4. 권한 부족 (403)

```json
{
  "success": false,
  "message": "접근 권한이 없습니다",
  "error": {
    "hscode": "FORBIDDEN",
    "code": "A002",
    "message": "해당 리소스에 접근할 권한이 없습니다",
    "details": "ADMIN 권한이 필요합니다",
    "timestamp": "2025-08-15T08:07:48",
    "path": "/api/admin/users"
  },
  "timestamp": "2025-08-15T08:07:48",
  "path": "/api/admin/users"
}
```

##### 5. 비즈니스 로직 오류 (409)

```json
{
  "success": false,
  "message": "비즈니스 규칙 위반",
  "error": {
    "hscode": "CONFLICT",
    "code": "B001",
    "message": "이미 존재하는 이메일입니다",
    "details": "hong@example.com은 이미 다른 사용자가 사용 중입니다",
    "timestamp": "2025-08-15T08:07:48",
    "path": "/api/users"
  },
  "timestamp": "2025-08-15T08:07:48",
  "path": "/api/users"
}
```

##### 6. 서버 내부 오류 (500)

```json
{
  "success": false,
  "message": "서버 내부 오류가 발생했습니다",
  "error": {
    "hscode": "INTERNAL_SERVER_ERROR",
    "code": "S001",
    "message": "예상치 못한 오류가 발생했습니다",
    "details": "시스템 관리자에게 문의하세요",
    "timestamp": "2025-08-15T08:07:48",
    "path": "/api/users"
  },
  "timestamp": "2025-08-15T08:07:48",
  "path": "/api/users"
}
    "code": "ACCESS_DENIED",
    "message": "해당 리소스에 접근할 권한이 없습니다",
    "details": "ADMIN 권한이 필요합니다",
    "timestamp": "2025-08-08T04:01:33Z",
    "path": "/api/admin/users"
  },
  "timestamp": "2025-08-08T04:01:33Z",
  "path": "/api/admin/users"
}
```

##### 5. 비즈니스 로직 오류 (409)

```json
{
  "success": false,
  "message": "비즈니스 규칙 위반",
  "error": {
    "code": "BUSINESS_RULE_VIOLATION",
    "message": "이미 존재하는 이메일입니다",
    "details": "hong@example.com은 이미 다른 사용자가 사용 중입니다",
    "timestamp": "2025-08-08T04:01:33Z",
    "path": "/api/users"
  },
  "timestamp": "2025-08-08T04:01:33Z",
  "path": "/api/users"
}
```

##### 6. 서버 내부 오류 (500)

```json
{
  "success": false,
  "message": "서버 내부 오류가 발생했습니다",
  "error": {
    "code": "INTERNAL_SERVER_ERROR",
    "message": "예상치 못한 오류가 발생했습니다",
    "details": "시스템 관리자에게 문의하세요",
    "timestamp": "2025-08-08T04:01:33Z",
    "path": "/api/users"
  },
  "timestamp": "2025-08-08T04:01:33Z",
  "path": "/api/users"
}
```

#### 📄 페이지네이션 응답

##### 페이지네이션 성공 응답
```json
{
  "success": true,
  "message": "사용자 목록 조회 성공",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "홍길동",
        "email": "hong@example.com"
      },
      {
        "id": 2,
        "name": "김철수",
        "email": "kim@example.com"
      }
    ],
    "pageable": {
      "page": 0,
      "size": 20,
      "sort": "createdAt,desc"
    },
    "totalElements": 100,
    "totalPages": 5,
    "first": true,
    "last": false,
    "hasNext": true,
    "hasPrevious": false,
  },
  "timestamp": "2025-08-08T04:01:33Z",
  "path": "/api/users"
}
```

#### 🌐 HTTP 상태 코드 가이드

##### 성공 응답 (2xx)

| 코드 | 의미 | 사용 사례 |
|------|------|----------|
| 200 | OK | 조회, 수정 성공 |
| 201 | Created | 생성 성공 |
| 204 | No Content | 삭제 성공 (응답 바디 없음) |

##### 클라이언트 오류 (4xx)

| 코드 | 의미 | 에러 코드 | 사용 사례 |
|------|------|-----------|----------|
| 400 | Bad Request | VALIDATION_FAILED | 유효성 검증 실패 |
| 401 | Unauthorized | AUTHENTICATION_FAILED | 인증 실패 |
| 403 | Forbidden | ACCESS_DENIED | 권한 부족 |
| 404 | Not Found | RESOURCE_NOT_FOUND | 리소스 없음 |
| 409 | Conflict | BUSINESS_RULE_VIOLATION | 비즈니스 규칙 위반 |
| 429 | Too Many Requests | RATE_LIMIT_EXCEEDED | 요청 한도 초과 |

##### 서버 오류 (5xx)

| 코드 | 의미 | 에러 코드 | 사용 사례 |
|------|------|-----------|----------|
| 500 | Internal Server Error | INTERNAL_SERVER_ERROR | 서버 내부 오류 |
| 502 | Bad Gateway | SERVICE_UNAVAILABLE | 외부 서비스 오류 |
| 503 | Service Unavailable | SERVICE_UNAVAILABLE | 서비스 일시 중단 |

#### 🌍 메시지 다국화

##### 메시지 키 구조

```
{domain}.{action}.{result}
{domain}.{validation}.{field}
error.{errorType}.{specificError}
```

##### 다국화 메시지 파일

###### messages.properties (기본, 한국어)
```properties
# 성공 메시지
user.create.success=사용자 생성 성공
user.update.success=사용자 정보 수정 성공
user.delete.success=사용자 삭제 성공
user.get.success=사용자 조회 성공
user.list.success=사용자 목록 조회 성공

# 유효성 검증 메시지
user.validation.name=이름은 필수입니다
user.validation.email=올바른 이메일 형식이 아닙니다
user.validation.age=나이는 1 이상 150 이하여야 합니다
user.validation.password=비밀번호는 8자 이상이어야 합니다

# 에러 메시지
error.user.notFound=사용자를 찾을 수 없습니다
error.user.alreadyExists=이미 존재하는 이메일입니다
error.user.inactiveUser=비활성화된 사용자입니다

# 공통 에러 메시지
error.authentication.failed=인증에 실패했습니다
error.authorization.denied=접근 권한이 없습니다
error.validation.failed=입력값 검증에 실패했습니다
error.internal.server=서버 내부 오류가 발생했습니다
```

###### messages_en.properties (영어)
```properties
# Success messages
user.create.success=User created successfully
user.update.success=User updated successfully
user.delete.success=User deleted successfully
user.get.success=User retrieved successfully
user.list.success=User list retrieved successfully

# Validation messages
user.validation.name=Name is required
user.validation.email=Invalid email format
user.validation.age=Age must be between 1 and 150
user.validation.password=Password must be at least 8 characters

# Error messages
error.user.notFound=User not found
error.user.alreadyExists=Email already exists
error.user.inactiveUser=User is inactive

# Common error messages
error.authentication.failed=Authentication failed
error.authorization.denied=Access denied
error.validation.failed=Validation failed
error.internal.server=Internal server error occurred
```


### 4. AxResponseEntity 표준 메서드 (최신 - ErrorCode 지원)
```java
// 주요 성공 응답 메서드들 (권장 사용)
AxResponseEntity.ok(data, message)                             // 200 OK
AxResponseEntity.okPage(pageResponse, message)                 // 200 OK
AxResponseEntity.created(data, message)                        // 201 CREATED
AxResponseEntity.updated(data, message)                        // 200 OK
AxResponseEntity.deleted(message)                              // 200 OK (삭제 성공)

// ErrorCode를 사용한 실패 응답 메서드들 (권장)
AxResponseEntity.error(errorCode)                              // ErrorCode 객체 사용
AxResponseEntity.error(errorCode, customMessage)              // ErrorCode + 커스텀 메시지
AxResponseEntity.notFound(errorCode)                           // 404 + ErrorCode
AxResponseEntity.badRequest(errorCode)                         // 400 + ErrorCode
AxResponseEntity.unauthorized(errorCode)                       // 401 + ErrorCode
AxResponseEntity.forbidden(errorCode)                          // 403 + ErrorCode
AxResponseEntity.conflict(errorCode)                           // 409 + ErrorCode
AxResponseEntity.internalServerError(errorCode)               // 500 + ErrorCode

// 메시지와 구체적 코드를 직접 지정하는 방법
AxResponseEntity.notFound(message, "U001")                     // 404 + 구체적 코드
AxResponseEntity.badRequest(message, "C002")                   // 400 + 구체적 코드

// 기존 방식 (하위 호환성 유지)
AxResponseEntity.badRequest(message)                          // 400 BAD REQUEST
AxResponseEntity.unauthorized(message)                        // 401 UNAUTHORIZED
AxResponseEntity.forbidden(message)                           // 403 FORBIDDEN
AxResponseEntity.notFound(message)                            // 404 NOT FOUND
AxResponseEntity.conflict(message)                            // 409 CONFLICT
AxResponseEntity.internalServerError(message)                 // 500 INTERNAL SERVER ERROR
```

### 5. ErrorCode 표준 정의 (최신)
```java
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 사용자 관련 (U001~U099)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다"),
    USER_INACTIVE(HttpStatus.FORBIDDEN, "U003", "비활성화된 사용자입니다"),
    
    // 샘플 관련 (S001~S099)
    SAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "샘플을 찾을 수 없습니다"),
    SAMPLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "S002", "이미 존재하는 샘플입니다"),
    
    // 공통 오류 (C001~C099)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "C002", "입력값 검증에 실패했습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청한 리소스를 찾을 수 없습니다"),
    
    // 인증/인가 오류 (A001~A099)
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A001", "인증에 실패했습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A003", "토큰이 만료되었습니다"),
    
    // 서버 오류 (E001~E099)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "서버 내부 오류가 발생했습니다"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.BAD_GATEWAY, "E002", "외부 서비스 오류가 발생했습니다");
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

### 6. 에러 응답 구조 표준 (최신)
```java
// AxError 클래스 구조
public class AxError {
    private String hscode;      // HTTP 상태 기반 코드 (예: "NOT_FOUND")
    private String code;        // ErrorCode 기반 구체적인 코드 (예: "U001")
    private String message;     // 에러 메시지
    private String details;     // 에러 상세 정보
    private String timestamp;   // 에러 발생 시간
    private String path;        // 요청 경로
    private List<FieldError> fieldErrors; // 유효성 검증 에러 목록
}

// AxResponse.failure 메서드 구조
AxResponse.failure(message, hscode, code, statusCode, statusText)
```

## 데이터베이스 설계 규칙

### 1. 테이블 명명 규칙
- 테이블명: snake_case, 복수형 (예: users, products, order_items)
- 컬럼명: snake_case (예: created_at, user_id, email_address)
- 인덱스명: idx_테이블명_컬럼명 (예: idx_users_email)

### 2. 공통 컬럼
```java
// 모든 엔티티에 포함할 공통 필드
@CreatedDate
@Column(name = "created_at", updatable = false)
private LocalDateTime createdAt;

@LastModifiedDate
@Column(name = "updated_at")
private LocalDateTime updatedAt;

@Column(name = "created_by", length = 50)
private String createdBy;

@Column(name = "updated_by", length = 50)
private String updatedBy;
```

### 3. JPA 엔티티 규칙
```java
@Entity
@Table(name = "table_name")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EntityName extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 나머지 필드들...
}
```

## 예외 처리 규칙

### 1. 커스텀 예외 계층구조
```java
// 기본 예외
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}

// 비즈니스 예외
public class BusinessException extends CustomException {
    // 비즈니스 로직 관련 예외
}

// 검증 예외
public class ValidationException extends CustomException {
    // 입력값 검증 관련 예외
}
```

### 2. ErrorCode 정의 패턴 (최신)
```java
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 사용자 관련 (U001~U099)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다"),
    USER_INACTIVE(HttpStatus.FORBIDDEN, "U003", "비활성화된 사용자입니다"),
    
    // 샘플 관련 (S001~S099)
    SAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "샘플을 찾을 수 없습니다"),
    SAMPLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "S002", "이미 존재하는 샘플입니다"),
    
    // 공통 오류 (C001~C099)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "C002", "입력값 검증에 실패했습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청한 리소스를 찾을 수 없습니다"),
    
    // 인증/인가 오류 (A001~A099)
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A001", "인증에 실패했습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A003", "토큰이 만료되었습니다"),
    
    // 서버 오류 (E001~E099)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "서버 내부 오류가 발생했습니다"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.BAD_GATEWAY, "E002", "외부 서비스 오류가 발생했습니다");
    
    private final HttpStatus status;  // HTTP 상태 정보 포함
    private final String code;        // 구체적인 에러 코드
    private final String message;     // 에러 메시지
}
```

### 3. GlobalExceptionHandler 업데이트
```java
// 최신 에러 응답 구조 사용
@ExceptionHandler(CustomException.class)
public ResponseEntity<AxResponse<Void>> handleCustomException(CustomException e, HttpServletRequest request) {
    ErrorCode errorCode = e.getErrorCode();
    
    // 에러 정보 구성
    AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
        .hscode(errorCode.getHttpStatusCode())
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .details(e.getDetails() != null ? e.getDetails() : errorCode.getMessage())
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .build();
    
    AxResponse<Void> response = AxResponse.failure(
        errorCode.getMessage(),
        errorInfo
    );
    
    return ResponseEntity.status(errorCode.getStatus()).body(response);
}

// 유효성 검증 실패 처리
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<AxResponse<Void>> handleValidationException(
        MethodArgumentNotValidException e, HttpServletRequest request) {
    
    List<AxResponse.FieldError> fieldErrors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> AxResponse.FieldError.builder()
            .field(error.getField())
            .rejectedValue(error.getRejectedValue())
            .message(error.getDefaultMessage())
            .build())
        .collect(Collectors.toList());
    
    AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
        .hscode("BAD_REQUEST")
        .code("V001")
        .message("입력값이 올바르지 않습니다")
        .details("필수 필드가 누락되었거나 형식이 올바르지 않습니다")
        .timestamp(LocalDateTime.now())
        .path(request.getRequestURI())
        .fieldErrors(fieldErrors)
        .build();
    
    AxResponse<Void> response = AxResponse.failure(
        "입력값 검증에 실패했습니다",
        errorInfo
    );
    
    return ResponseEntity.badRequest().body(response);
}
```

## 테스트 코드 작성 규칙

### 1. 테스트 클래스 구조
```java
@DisplayName("사용자 서비스 테스트")
class UserServiceTest {
    
    // given-when-then 패턴 사용
    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() {
        // given
        UserCreateRequest request = createUserRequest();
        
        // when
        UserResponse result = userService.createUser(request);
        
        // then
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
    }
    
    // 테스트 헬퍼 메서드는 private으로 하단에 배치
    private UserCreateRequest createUserRequest() {
        // 테스트 데이터 생성 로직
    }
}
```

### 2. Mock 사용 규칙
```java
// given 절에서 Mock 동작 정의
given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

// then 절에서 검증
verify(userRepository).save(any(User.class));
verify(userRepository, times(1)).findById(userId);
```
### 테스트 커버리지 규정 및 관리

- **목표 커버리지**: 전체 프로젝트 라인 커버리지 80% 이상
- **커버리지 측정 도구**: JaCoCo, IntelliJ Coverage, SonarQube 등 사용 권장
- **커버리지 종류**:
  - **라인(Line) 커버리지**: 전체 코드 라인 중 테스트가 실행된 라인의 비율
  - **브랜치(Branch) 커버리지**: 조건문(if/else, switch 등)의 각 분기마다 테스트가 실행된 비율
  - **클래스/메서드 커버리지**: 전체 클래스/메서드 중 테스트가 실행된 클래스/메서드의 비율
- **테스트 포함 범위**:
  - Service, Repository, Controller 레이어의 주요 비즈니스 로직
  - Exception 및 Validation 처리
  - API 응답/에러 케이스
- **커버리지 관리 방법**:
  - 커버리지 리포트 자동 생성 (빌드 시 reports 디렉터리 내 jacocoTestReport.html 등)
  - 커버리지 하락 시 PR 리뷰에서 원인 분석 후 보완
  - SonarQube 등 외부 품질 관리 도구를 통한 지속적 모니터링 권장
- **테스트 커버리지 향상 방법**:
  - MockMvc를 이용한 Controller 통합 테스트 작성
  - 예외 발생/경계값/빈 값 등 다양한 케이스 테스트
  - 커버리지 미만 부분은 추가 테스트 작성
- **커버리지 예시 리포트**
    - 라인 커버리지: 85%
    - 브랜치 커버리지: 80%
    - 클래스 커버리지: 100%

## 로깅 규칙

### 1. 로그 레벨 사용 기준
```java
log.error("시스템 오류", exception);     // 시스템 오류
log.warn("비정상 상황이지만 처리 가능");   // 경고
log.info("중요한 비즈니스 이벤트");       // 정보
log.debug("디버깅 정보");              // 디버그
```

### 2. 로그 메시지 형식
```java
// 성공 로그
log.info("Created new user with id: {}", savedUser.getId());

// 오류 로그
log.error("Failed to create user with email: {}, error: {}", 
          request.getEmail(), exception.getMessage());

// 비즈니스 로그
log.info("User {} successfully logged in", user.getEmail());
```

## 보안 규칙

### 1. 인증/인가 처리
```java
// JWT 토큰 검증
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public UserResponse getUserProfile() {
    // 구현
}

// 자원 소유자 검증
@PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
public UserResponse updateUser(@PathVariable Long userId, ...) {
    // 구현
}
```

### 2. 민감한 정보 처리
```java
// 비밀번호는 로그에 출력하지 않음
@ToString.Exclude
private String password;

// API 응답에서 민감한 정보 제외
@JsonIgnore
private String password;
```

## 성능 최적화 규칙

### 1. 데이터베이스 쿼리 최적화
```java
// N+1 문제 방지를 위한 Fetch Join 사용
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.active = true")
List<User> findActiveUsersWithOrders();

// 페이징 쿼리 최적화
@Query(value = "SELECT u FROM User u WHERE u.name LIKE %:name%",
       countQuery = "SELECT count(u) FROM User u WHERE u.name LIKE %:name%")
Page<User> findByNameContaining(@Param("name") String name, Pageable pageable);
```

### 2. 캐싱 전략
```java
@Cacheable(value = "users", key = "#id")
public UserResponse getUserById(Long id) {
    // 구현
}

@CacheEvict(value = "users", key = "#result.id")
public UserResponse updateUser(Long id, UserUpdateRequest request) {
    // 구현
}
```

## 문서화 규칙

### 1. API 문서화
```java
@Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
@ApiResponses({
    @ApiResponse(responseCode = "201", description = "사용자 생성 성공"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
    @ApiResponse(responseCode = "409", description = "이메일 중복")
})
public AxResponseEntity<UserResponse> createUser(
        @Valid @RequestBody UserCreateRequest request) {
    // 구현
}
```

### 2. 클래스 문서화
```java
/**
 * 사용자 관리 서비스
 * 
 * <p>사용자의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 처리합니다.
 * 이메일 중복 검증, 비밀번호 암호화 등의 기능을 포함합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-07-19
 * @version 1.0
 */
@Service
public class UserService {
    // 구현
}
```

## 특별 지시사항

### 1. 코드 생성 우선순위
1. 보안을 최우선으로 고려
2. 예외 처리를 반드시 포함
3. 로깅 구문 적절히 삽입
4. 테스트 가능한 구조로 설계
5. 성능을 고려한 구현
6. 애플리케이션 상수 클래스(Constants)를 생성하여 사용
7. sample소스는 아래와 같이 sample디렉토리에 생성
8. Controller에서 사용하는 DTO(/dto/** 디렉토리)생성시 
   - Request는 Req 접미사 사용하고 Request 디렉토리에 생성
   - Response는 Res 접미사 사용하고 Response 디렉토리에 생성
   - 이외는 기본 디렉토리에 생성
9. Feign Client에서 사용하는 DTO(/client/** 디렉토리) 생성시 
   - File명에 Req, Request, req, request가 포함된 경우 Request 디렉토리에 생성
   - File명에 Res, Response, res, response가 포함된 경우 Response 디렉토리에 생성
   - 이외는 기본 디렉토리에 생성

```
controller: controller/sample
service: service/sample, service/sample/Impl
entity: entity/sample
repository: repository/sample
dto: dto/sample/request, dto/sample/response
mapper: mapper/sample
```

### 2. 응답 형식 최신 규칙 (중요)
```java
// ✅ 최신 방식 (필수 사용)
AxResponseEntity<PageResponse<T>>

// ✅ 최신 AxResponse 구조 (필수 사용)
private final Integer statusCode;
private final String statusText;

// ✅ 최신 패키지 구조 (필수 사용)
com.skax.core.common.response.PageResponse
```

### 3. 금지사항
- System.out.println() 사용 금지 (로깅 프레임워크 사용)
- Raw Type 사용 금지 (제네릭 타입 명시)
- Magic Number 사용 금지 (상수로 정의)
- 하드코딩된 문자열 사용 최소화
- try-catch로 예외 숨기기 금지
- **dto.common 패키지 사용 금지** (common.response 패키지 사용 필수)
- **Controller에서 Feign Client DTO 사용 금지** (Service 계층에서 처리)
- **Controller에서 Entity 사용 금지** (Service 계층에서 처리)
- **DevTools를 운영 환경에 포함 금지** (개발 환경 전용)

### 4. 권장사항
- Optional 적극 활용으로 NPE 방지
- Stream API 활용한 함수형 프로그래밍
- Builder 패턴 사용으로 객체 생성 명확화
- 인터페이스 기반 설계로 확장성 확보
- 단위 테스트 커버리지 80% 이상 유지
- Mocking 프레임워크 사용으로 외부 의존성 최소화
- Lombok을 활용한 코드 간결화
- MapStruct를 통한 DTO와 Entity 간 변환 최적화
- OpenAPI 3를 통한 API 문서화 자동화
- **PageResponse를 통한 클라이언트 친화적 페이징 응답**
- **AxResponseEntity의 ok() 메서드 적극 활용**
- **statusCode/statusText 기반 일관된 오류 응답**

## 프로젝트별 특수 요구사항

### 1. AXCORE BACKEND 개인 프로젝트 규칙
- 모든 커밋 메시지는 한글로 작성
- API 응답 메시지는 한글로 제공
- 에러 메시지도 사용자 친화적인 한글로 작성
- 주석은 한글로 작성하되 기술적 용어는 영어 병기

### 2. 개발 환경 설정
- 로컬: H2 인메모리 데이터베이스 사용
- 외부개발: PostGreSQL 사용 및 TestContainers 활용한 실제 DB 테스트
- 개발: PostGreSQL 사용 및 TestContainers 활용한 실제 DB 테스트
- 스테이징: PostGreSQL 사용
- 운영: PostGreSQL 사용
- 개발적용시 Docker Compose 활용

#### Spring Boot DevTools 설정
- **개발 환경 전용**: 운영 환경에서는 자동으로 비활성화
- **자동 재시작**: 클래스패스 파일 변경 시 애플리케이션 자동 재시작
- **라이브 리로드**: 정적 리소스 변경 시 브라우저 자동 새로고침
- **JMX 비활성화**: DevTools JMX 연결 오류 방지를 위해 기본적으로 JMX 비활성화
- **설정 파일**: application-elocal.yml에 DevTools 관련 설정 포함

**DevTools 의존성 추가**:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**DevTools 설정 예시** (application-elocal.yml):
```yaml
spring:
  jmx:
    enabled: false  # JMX 연결 오류 방지
  devtools:
    restart:
      enabled: true
      additional-paths: src/main/resources
      exclude: static/**,public/**,templates/**
    livereload:
      enabled: true
      port: 35729
    add-properties: true
```

**JVM 옵션으로 JMX 완전 비활성화**:
```bash
-Dcom.sun.management.jmxremote=false
-Dspring.jmx.enabled=false
```

### 3. CI/CD 관련
- GitHub Actions를 통한 자동 빌드/테스트
- PR시 코드 리뷰 필수
- main 브랜치 직접 푸시 금지
- 태그 기반 배포 전략 사용

## 환경별 설정 가이드

### 1. Local 환경 (local)
```yaml
database: H2 in-memory
logging_level: DEBUG
security: 완화된 설정
cache: 비활성화
```

### 2. 개발 환경 (dev)
```yaml
database: PostGreSQL (개발 DB)
logging_level: DEBUG
security: 완화된 설정
cache: 비활성화
```

### 3. 스테이징 환경 (staging)
```yaml
database: PostGreSQL (테스트 DB)
logging_level: INFO
security: 운영과 동일
cache: 활성화
```

### 4. 운영 환경 (prod)
```yaml
database: PostGreSQL (운영 DB)
logging_level: WARN
security: 강화된 설정
cache: 활성화
monitoring: 전체 활성화
```

## 팀 개발 규칙

### 1. 코드 리뷰 체크리스트
- [ ] 보안 취약점 검토 완료
- [ ] 예외 처리 적절성 확인
- [ ] 테스트 코드 포함 여부
- [ ] 성능 영향도 검토
- [ ] 문서화 완료
- [ ] **PageResponse 사용 여부 확인** (Page<T> 대신)
- [ ] **AxResponse 최신 구조 사용 여부 확인** (statusCode/statusText)

### 2. 브랜치 전략
```
main: 운영 배포 브랜치
develop: 개발 통합 브랜치
feature/기능명: 기능 개발 브랜치
hotfix/이슈번호: 긴급 수정 브랜치
```

### 3. 커밋 메시지 규칙
- 한글 사용: 모든 메시지는 한글로 작성합니다.
- 명확성: 커밋이 무엇을 변경했는지, 왜 변경했는지 명확하게 전달합니다.
- 일관성: 프로젝트 내에서 정한 규칙을 일관성 있게 따릅니다.
- 제목 (Subject Line)
   - 형식: [타입]: [간결한 요약 (명령형 어조)]
   - 길이: 50자 이내 (권장)
   - 명령형 어조 사용: "~을 추가", "~을 수정", "~을 제거" 와 같이 동사로 시작합니다. (예: "기능 추가", "버그 수정")
   - 마침표 사용 금지: 제목 끝에는 마침표를 찍지 않습니다.

```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅
refactor: 코드 리팩토링 (PageResponse 적용, AxResponse 구조 변경 등)
test: 테스트 코드 추가/수정
chore: 기타 작업
```

## 성과 지표
- 코드 일관성: 90% 이상
- 예외 처리 포함률: 95% 이상
- 테스트 커버리지: 80% 이상
- 보안 규칙 준수율: 100%
- 문서화 완성도: 85% 이상
- **PageResponse 적용률: 100%** (새로 추가)
- **최신 AxResponse 구조 적용률: 100%** (새로 추가)

## AI 코딩 최적화 설정

### 1. Copilot 코드 생성 우선순위 (최신)
```yaml
priorities:
  - security_first: true
  - performance_aware: true
  - test_driven: true
  - documentation_included: true
  - korean_comments: true
  - page_response_required: true      # PageResponse 필수 사용
  - latest_ax_response: true          # 최신 AxResponse 구조 사용
  - unified_response_format: true     # 통합 응답 형식 사용
```

### 2. 템플릿 우선순위 (최신)
```java
// 엔티티 생성시 반드시 포함할 패턴
@Entity
@Table(name = "테이블명")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EntityName extends BaseEntity {
    // 구현
}

// Controller 페이징 메서드 필수 패턴
@GetMapping
public AxResponseEntity<PageResponse<EntityResponse>> getEntities(
        @PageableDefault(size = 20) Pageable pageable) {
    PageResponse<EntityResponse> entities = entityService.getEntities(pageable);
    return AxResponseEntity.ok(entities, "목록을 성공적으로 조회했습니다.");
}

// Service 페이징 메서드 필수 패턴
@Transactional(readOnly = true)
public PageResponse<EntityResponse> getEntities(Pageable pageable) {
    Page<Entity> entityPage = entityRepository.findAll(pageable);
    Page<EntityResponse> responsePage = entityPage.map(entityMapper::toResponse);
    return PageResponse.from(responsePage);
}
```

### 3. 의존성 주입 패턴
```java
// Constructor Injection 우선 사용
@RequiredArgsConstructor
public class ServiceClass {
    private final Repository repository;
    // Field Injection 금지, Setter Injection 최소화
}
```

### 4. Import 패턴 (최신)
```java
// 필수 import 패턴
import com.skax.core.common.response.AxResponseEntity;
import com.skax.core.common.response.PageResponse;
// dto.common.PageResponse 사용 금지
```

```mermaid
graph LR
    A[시작] --> B(원형);
    B --> C{조건?};
    C -- 예 --> D(참);
    C -- 아니오 --> E(거짓);
```