package com.skax.core.util;

import com.skax.core.dto.BaseDto;
import com.skax.core.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 서비스 공통 유틸리티 클래스
 * 
 * <p>서비스 레이어에서 공통으로 사용되는 기능을 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-09-02
 * @version 1.0
 */
@Component
public class ServiceUtils {

    @Autowired
    private AuditMapper auditMapper;

    /**
     * 엔티티를 DTO로 변환하면서 감사 정보를 매핑합니다.
     * 
     * @param <T> DTO 타입
     * @param entity 소스 엔티티
     * @param dto 대상 DTO
     * @return 감사 정보가 매핑된 DTO
     */
    public <T extends BaseDto> T mapWithAudit(BaseEntity entity, T dto) {
        if (entity != null && dto != null) {
            auditMapper.mapAuditFields(entity, dto);
        }
        return dto;
    }
}
