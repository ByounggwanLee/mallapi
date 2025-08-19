package com.skax.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
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
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server().url("http://localhost:8080").description("개발 서버"),
                    new Server().url("https://api.mallapi.com").description("운영 서버")
                ))
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", 
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT 토큰을 입력하세요"))
                    .addSecuritySchemes("oauth2", 
                        new SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .description("OAuth2 인증")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
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
                        
                        ## 응답 형식 표준화 (AxResponse)
                        
                        모든 API 응답은 `AxResponse` 구조를 따르며, 다음과 같은 표준 형식을 사용합니다:
                        
                        ### 성공 응답
                        ```json
                        {
                          "success": true,
                          "message": "성공 메시지",
                          "data": { /* 실제 데이터 */ },
                          "timestamp": "2025-08-19T10:30:00",
                          "statusCode": 200,
                          "statusText": "OK"
                        }
                        ```
                        
                        ### 실패 응답
                        ```json
                        {
                          "success": false,
                          "message": "에러 메시지",
                          "error": {
                            "hscode": "NOT_FOUND",
                            "code": "U001",
                            "message": "사용자를 찾을 수 없습니다",
                            "details": "상세 에러 정보",
                            "timestamp": "2025-08-19T10:30:00",
                            "path": "/api/v1/users/123"
                          },
                          "timestamp": "2025-08-19T10:30:00",
                          "statusCode": 404,
                          "statusText": "Not Found"
                        }
                        ```
                        
                        ## 인증
                        
                        이 API는 JWT 기반 인증을 사용합니다. 보안이 필요한 엔드포인트에 접근하려면 
                        Authorization 헤더에 Bearer 토큰을 포함해야 합니다.
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
}
