package com.skax.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) 설정 클래스
 * 
 * <p>웹 애플리케이션에서 다른 도메인으로부터의 API 요청을 허용하기 위한 CORS 설정을 제공합니다.
 * 개발 환경과 운영 환경에 따라 다른 설정을 적용할 수 있습니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-21
 * @version 1.0
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "app.cors")
public class CorsConfig implements WebMvcConfigurer {

    private List<String> allowedOrigins = List.of(
            "http://localhost:3000",
            "http://localhost:3001", 
            "http://localhost:8080",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:3001",
            "http://127.0.0.1:8080"
    );
    
    private List<String> allowedMethods = List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
    );
    
    private String allowedHeaders = "*";
    private boolean allowCredentials = true;
    private long maxAge = 3600L;

    /**
     * CORS 설정을 등록합니다.
     * 
     * @param registry CORS 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("CORS 설정 시작");
        log.info("허용된 Origins: {}", allowedOrigins);
        log.info("허용된 Methods: {}", allowedMethods);
        log.info("허용된 Headers: {}", allowedHeaders);
        log.info("Credentials 허용: {}", allowCredentials);
        log.info("Max Age: {}초", maxAge);

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.toArray(new String[0]))
                .allowedMethods(allowedMethods.toArray(new String[0]))
                .allowedHeaders(allowedHeaders)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);

        log.info("CORS 설정 완료");
    }

    // Getters and Setters for ConfigurationProperties
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(long maxAge) {
        this.maxAge = maxAge;
    }
}
