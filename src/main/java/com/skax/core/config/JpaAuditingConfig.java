package com.skax.core.config;

import com.skax.core.entity.member.Member;
import com.skax.core.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * JPA Auditing 설정 클래스
 * 
 * <p>Spring Data JPA의 Auditing 기능을 활성화하고,
 * 현재 로그인한 사용자 정보를 자동으로 엔티티에 설정합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-09-02
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
@Slf4j
public class JpaAuditingConfig {

    private final MemberRepository memberRepository;

    /**
     * AuditorAware Bean 등록
     * 현재 로그인한 사용자의 Member 엔티티를 반환합니다.
     * 
     * @return AuditorAware<Member> 구현체
     */
    @Bean
    public AuditorAware<Member> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * AuditorAware 구현 클래스
     * Spring Security 컨텍스트에서 현재 사용자 정보를 가져와 Member 엔티티로 변환합니다.
     */
    private class AuditorAwareImpl implements AuditorAware<Member> {

        @Override
        @NonNull
        public Optional<Member> getCurrentAuditor() {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                
                if (authentication == null || !authentication.isAuthenticated() || 
                    "anonymousUser".equals(authentication.getPrincipal())) {
                    return getSystemUser();
                }

                String username = authentication.getName();
                if (username == null || username.trim().isEmpty()) {
                    return getSystemUser();
                }

                // 사용자 이메일로 Member 엔티티 조회
                return memberRepository.findById(username);
                
            } catch (Exception e) {
                log.warn("현재 사용자 정보를 가져오는데 실패했습니다: {}", e.getMessage());
                return getSystemUser();
            }
        }

        /**
         * 시스템 사용자 정보를 반환합니다.
         * 인증되지 않은 상황이나 오류 발생 시 사용됩니다.
         * 
         * @return 시스템 사용자 Member 엔티티
         */
        private Optional<Member> getSystemUser() {
            try {
                return memberRepository.findById("system@skax.core");
            } catch (Exception e) {
                log.warn("시스템 사용자 정보를 가져오는데 실패했습니다: {}", e.getMessage());
                return Optional.empty();
            }
        }
    }
}
