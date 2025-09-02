package com.skax.core.config;

import com.skax.core.entity.member.Member;
import com.skax.core.entity.member.MemberRole;
import com.skax.core.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * 초기 데이터 설정 클래스
 * 
 * <p>애플리케이션 시작 시 필요한 기본 데이터를 생성합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-09-02
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializationConfig {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 시스템 사용자 초기화
     * JPA Auditing에서 사용할 시스템 사용자를 생성합니다.
     * 
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner initializeSystemUser() {
        return args -> {
            String systemEmail = "system@skax.core";
            
            if (!memberRepository.existsByEmail(systemEmail)) {
                Member systemUser = Member.builder()
                        .email(systemEmail)
                        .pw(passwordEncoder.encode("system123!"))
                        .nickname("System")
                        .social(false)
                        .isActive(true)
                        .memberRoleList(List.of(MemberRole.ADMIN))
                        .build();
                
                memberRepository.save(systemUser);
                log.info("시스템 사용자가 생성되었습니다: {}", systemEmail);
            } else {
                log.info("시스템 사용자가 이미 존재합니다: {}", systemEmail);
            }
        };
    }
}
