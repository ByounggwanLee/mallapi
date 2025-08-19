package com.skax.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Mall API 메인 애플리케이션 클래스
 * 
 * <p>Spring Boot MallApi(todo,product,member,memberRole,JWT/OAuth2 Login) RESTful API 애플리케이션의 
 * 진입점입니다. 이 클래스는 Spring Boot 애플리케이션을 시작하고 필요한 기능들을 활성화합니다.</p>
 * 
 * <p>활성화되는 기능들:</p>
 * <ul>
 *   <li>Spring Boot 자동 설정</li>
 *   <li>OpenFeign 클라이언트</li>
 *   <li>JPA Auditing (생성일시, 수정일시 자동 관리)</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class MallApiApplication {

    /**
     * 애플리케이션 메인 메서드
     * 
     * @param args 명령행 인수
     */
    public static void main(String[] args) {
        log.info("=".repeat(80));
        log.info("Mall API Application Starting...");
        log.info("Start Time: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("Command Line Args: {}", args.length > 0 ? Arrays.toString(args) : "None");
        log.info("=".repeat(80));
        
        try {
            SpringApplication.run(MallApiApplication.class, args);
        } catch (Exception e) {
            // DevTools의 SilentExitException은 정상적인 재시작 과정이므로 무시
            if (e.getClass().getSimpleName().contains("SilentExitException")) {
                return;
            }
            log.error("Application startup failed: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * 애플리케이션 준비 완료 이벤트 리스너
     * 애플리케이션이 완전히 시작된 후 호출됩니다.
     */
    @Component
    @Slf4j
    static class ApplicationStartupLogger implements ApplicationRunner {

        private final Environment environment;

        public ApplicationStartupLogger(Environment environment) {
            this.environment = environment;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            logApplicationInfo();
        }

        @EventListener(ApplicationReadyEvent.class)
        public void onApplicationReady() {
            log.info("=".repeat(80));
            log.info("✅ Mall API Application 시작 완료!");
            log.info("완료 시간: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            log.info("=".repeat(80));
        }

        private void logApplicationInfo() {
            try {
                String protocol = "http";
                if (environment.getProperty("server.ssl.key-store") != null) {
                    protocol = "https";
                }

                String serverPort = environment.getProperty("server.port", "8080");
                String contextPath = environment.getProperty("server.servlet.context-path", "");
                String hostAddress = InetAddress.getLocalHost().getHostAddress();
                String[] activeProfiles = environment.getActiveProfiles();
                String profiles = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";

                log.info("=".repeat(80));
                log.info("🌐 Mall API Application 정보");
                log.info("=".repeat(80));
                log.info("📍 Local URL:    {}://localhost:{}{}", protocol, serverPort, contextPath);
                log.info("📍 External URL: {}://{}:{}{}", protocol, hostAddress, serverPort, contextPath);
                log.info("📍 Profile(s):   {}", profiles);
                log.info("📍 API Docs:     {}://localhost:{}{}/swagger-ui/index.html", protocol, serverPort, contextPath);
                log.info("📍 H2 Console:   {}://localhost:{}{}/h2-console (local 프로파일)", protocol, serverPort, contextPath);
                log.info("📍 JVM Version:  {}", System.getProperty("java.version"));
                log.info("📍 Spring Boot:  {}", environment.getProperty("spring.boot.version", "Unknown"));
                log.info("=".repeat(80));

            } catch (UnknownHostException e) {
                log.warn("호스트 주소를 가져올 수 없습니다: {}", e.getMessage());
            }
        }
    }
}
