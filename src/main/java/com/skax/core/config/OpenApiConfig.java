package com.skax.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) 설정 클래스
 * 
 * <p>Spring Boot 애플리케이션의 REST API 문서화를 위한 OpenAPI 3.0 설정을 제공합니다.
 * 표준화된 응답 형식(AxResponse)에 대한 문서화와 보안 설정을 포함합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    /**
     * OpenAPI 3.0 설정을 구성합니다.
     * 
     * @return OpenAPI 설정 객체
     */
    @Bean
    public OpenAPI mallApiOpenAPI() {
        String localUrl = "http://localhost:" + serverPort + contextPath;
        String externalUrl = "https://api.mallapi.com" + contextPath;
        
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server().url(localUrl).description("로컬 개발 서버"),
                    new Server().url(externalUrl).description("운영 서버")
                ))
                .tags(List.of(
                    new Tag().name("Samples").description("샘플 API - 기본 샘플 데이터 관리"),
                    new Tag().name("Todos").description("할 일 API - Todo 작업 관리"),
                    new Tag().name("Roles").description("역할 API - 사용자 권한 관리")
                ))
                .components(new Components()
                    // JWT Bearer 인증만 포함
                    .addSecuritySchemes("bearerAuth", 
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT 토큰을 입력하세요")));
    }

    /**
     * API 정보를 정의합니다.
     * 
     * @return API 정보
     */
    private Info apiInfo() {
        return new Info()
                .title("Mall API")
                .description("""
                        # Mall API Documentation
                        
                        이 문서는 Mall API의 전체 엔드포인트에 대한 설명을 제공합니다.
                        
                        ## 🚀 주요 기능
                        
                        - **Todo 관리**: 할 일 생성, 조회, 수정, 삭제
                        - **샘플 관리**: 기본 샘플 데이터 관리
                        - **권한 관리**: 역할 기반 접근 제어
                        
                        ## 📋 API 그룹 안내
                        
                        - **🌐 전체 API**: 모든 API 엔드포인트
                        - **📋 Samples API**: 샘플 데이터 관리
                        - **✅ Todos API**: 할 일 관리
                        - **🌍 Public API**: 인증 불요 공개 API
                        
                        ## 📝 응답 형식 표준화 (AxResponse)
                        
                        모든 API 응답은 `AxResponse` 구조를 따르며, 다음과 같은 표준 형식을 사용합니다:
                        
                        ### ✅ 성공 응답
                        ```json
                        {
                          "success": true,
                          "message": "성공 메시지",
                          "data": { 
                            "id": 1,
                            "name": "샘플 데이터"
                          },
                          "timestamp": "2025-08-21T10:30:00",
                          "statusCode": 200,
                          "statusText": "OK"
                        }
                        ```
                        
                        ### 📄 페이징 응답
                        ```json
                        {
                          "success": true,
                          "message": "목록 조회 성공",
                          "data": {
                            "content": [/* 데이터 배열 */],
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
                            "hasPrevious": false
                          },
                          "timestamp": "2025-08-21T10:30:00",
                          "statusCode": 200,
                          "statusText": "OK"
                        }
                        ```
                        
                        ### ❌ 실패 응답
                        ```json
                        {
                          "success": false,
                          "message": "에러 메시지",
                          "error": {
                            "hscode": "NOT_FOUND",
                            "code": "U001",
                            "message": "사용자를 찾을 수 없습니다",
                            "details": "ID 123에 해당하는 사용자가 존재하지 않습니다",
                            "timestamp": "2025-08-21T10:30:00",
                            "path": "/api/v1/users/123",
                            "fieldErrors": [
                              {
                                "field": "email",
                                "rejectedValue": "invalid-email",
                                "message": "올바른 이메일 형식이 아닙니다"
                              }
                            ]
                          },
                          "timestamp": "2025-08-21T10:30:00",
                          "statusCode": 404,
                          "statusText": "Not Found"
                        }
                        ```
                        
                        ## 🔐 인증 방식
                        
                        ### 1. JWT Bearer Token
                        ```
                        Authorization: Bearer <JWT_TOKEN>
                        ```
                        
                        ### 2. API Key
                        ```
                        X-API-KEY: <YOUR_API_KEY>
                        ```
                        
                        ### 3. OAuth2 (소셜 로그인)
                        - Google OAuth2
                        - Naver OAuth2
                        - Kakao OAuth2
                        
                        ### 4. Basic Auth (개발 환경 전용)
                        ```
                        Authorization: Basic <base64(username:password)>
                        ```
                        
                        ## 📊 HTTP 상태 코드
                        
                        | 코드 | 의미 | 설명 |
                        |------|------|------|
                        | 200 | OK | 요청 성공 |
                        | 201 | Created | 리소스 생성 성공 |
                        | 400 | Bad Request | 잘못된 요청 |
                        | 401 | Unauthorized | 인증 실패 |
                        | 403 | Forbidden | 권한 부족 |
                        | 404 | Not Found | 리소스 없음 |
                        | 409 | Conflict | 리소스 충돌 |
                        | 500 | Internal Server Error | 서버 오류 |
                        
                        ## 🚀 시작하기
                        
                        1. **인증**: 로그인 API를 통해 JWT 토큰 획득
                        2. **API 호출**: Authorization 헤더에 토큰 포함
                        3. **응답 확인**: 표준화된 AxResponse 형식으로 결과 확인
                        
                        ## 🔍 API 탐색 팁
                        
                        - 상단의 그룹 선택기로 원하는 API 카테고리 선택
                        - 각 엔드포인트의 "Try it out" 버튼으로 실제 테스트 가능
                        - 인증이 필요한 API는 우상단 "Authorize" 버튼으로 토큰 설정
                        """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("ByounggwanLee")
                    .email("byounggwan.lee@example.com")
                    .url("https://github.com/byounggwan"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"));
    }

    /**
     * 전체 API 그룹 설정
     * 
     * @return 전체 API 그룹 설정
     */
    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .displayName("🌐 전체 API")
                .pathsToMatch("/api/**")
                .build();
    }

    /**
     * Samples API 그룹 설정
     * 
     * @return Samples API 그룹 설정
     */
    @Bean
    public GroupedOpenApi samplesApi() {
        return GroupedOpenApi.builder()
                .group("samples")
                .displayName("📋 Samples API")
                .pathsToMatch("/api/**/sample/**")
                .packagesToScan("com.skax.core.controller.sample")
                .build();
    }

    /**
     * Todos API 그룹 설정
     * 
     * @return Todos API 그룹 설정
     */
    @Bean
    public GroupedOpenApi todosApi() {
        return GroupedOpenApi.builder()
                .group("todos")
                .displayName("✅ Todos API")
                .pathsToMatch("/api/**/todos/**")
                .packagesToScan("com.skax.core.controller.todo")
                .build();
    }

    /**
     * Public API 그룹 설정 (인증이 필요없는 API)
     * 
     * @return Public API 그룹 설정
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .displayName("🌍 Public API")
                .pathsToMatch("/api/**/public/**", "/api/health", "/api/info")
                .build();
    }
}