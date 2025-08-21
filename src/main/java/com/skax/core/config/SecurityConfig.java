package com.skax.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 설정 클래스
 * 
 * <p>애플리케이션의 보안 설정을 담당합니다.</p>
 * <p>프로파일별로 다른 보안 정책을 적용합니다:</p>
 * <ul>
 *   <li>local, dev: Swagger UI, H2 Console 접근 허용</li>
 *   <li>prod: 엄격한 보안 정책 적용</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Environment environment;

    public SecurityConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * Spring Security 필터 체인 설정
     * 
     * @param http HttpSecurity 설정 객체
     * @return SecurityFilterChain 보안 필터 체인
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isLocalOrDev = Arrays.stream(activeProfiles)
                .anyMatch(profile -> "local".equals(profile) || "dev".equals(profile));

        log.info("Security Configuration - Active Profiles: {}", Arrays.toString(activeProfiles));
        log.info("Security Configuration - Local/Dev Mode: {}", isLocalOrDev);

        if (isLocalOrDev) {
            // 개발 환경: 대부분의 엔드포인트에 대해 접근 허용
            return configureForDevelopment(http);
        } else {
            // 운영 환경: 엄격한 보안 정책
            return configureForProduction(http);
        }
    }

    /**
     * 개발 환경용 보안 설정
     * 
     * @param http HttpSecurity 설정 객체
     * @return SecurityFilterChain 보안 필터 체인
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    private SecurityFilterChain configureForDevelopment(HttpSecurity http) throws Exception {
        log.info("Configuring Security for Development Environment - Permitting all API requests");
        
        return http
                // CSRF 비활성화 (개발 환경)
                .csrf(AbstractHttpConfigurer::disable)
                
                // CORS 설정 (개발 환경)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                
                // 세션 정책: STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Headers 설정 (H2 Console을 위한 frame options 허용)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                
                // 권한 설정 - 개발 환경에서는 모든 요청 허용
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                
                // 기본 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(AbstractHttpConfigurer::disable)
                
                .build();
    }

    /**
     * 운영 환경용 보안 설정
     * 
     * @param http HttpSecurity 설정 객체
     * @return SecurityFilterChain 보안 필터 체인
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    private SecurityFilterChain configureForProduction(HttpSecurity http) throws Exception {
        log.info("Configuring Security for Production Environment");
        
        return http
                // CSRF 활성화 (운영 환경)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                
                // CORS 설정 (운영 환경)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                
                // 세션 정책: STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Headers 보안 강화
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .contentTypeOptions(contentType -> {})
                        .httpStrictTransportSecurity(hsts -> hsts
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true)))
                
                // 권한 설정 (운영 환경은 엄격)
                .authorizeHttpRequests(auth -> auth
                        // 공개 API만 허용
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/health"),
                                new AntPathRequestMatcher("/api/actuator/health")
                        ).permitAll()
                        
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated())
                
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/api/login")
                        .defaultSuccessUrl("/api/", true))
                
                .build();
    }

    /**
     * CORS 설정 소스를 생성합니다.
     * 
     * @return CorsConfigurationSource CORS 설정 소스
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("CORS 설정 소스 생성");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 허용할 Origin 설정 (환경에 따라 동적 설정)
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isLocalOrDev = Arrays.stream(activeProfiles)
                .anyMatch(profile -> "local".equals(profile) || "dev".equals(profile));
        
        if (isLocalOrDev) {
            // 개발 환경: 로컬 개발용 Origin 허용
            configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001", 
                "http://localhost:8080",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:3001",
                "http://127.0.0.1:8080"
            ));
        } else {
            // 운영 환경: 운영 도메인만 허용
            configuration.setAllowedOrigins(List.of(
                "https://mallapi.com",
                "https://www.mallapi.com"
            ));
        }
        
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // 허용할 헤더
        configuration.setAllowedHeaders(List.of("*"));
        
        // 자격 증명 허용
        configuration.setAllowCredentials(true);
        
        // 캐시 시간 설정 (1시간)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS 설정 완료 - Allowed Origins: {}", configuration.getAllowedOrigins());
        
        return source;
    }
}
