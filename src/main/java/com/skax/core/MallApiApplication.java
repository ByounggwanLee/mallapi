package com.skax.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
        SpringApplication.run(MallApiApplication.class, args);
    }
}
