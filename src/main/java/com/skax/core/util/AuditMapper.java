package com.skax.core.util;

import com.skax.core.dto.AuditDto;
import com.skax.core.dto.BaseDto;
import com.skax.core.entity.BaseEntity;
import com.skax.core.entity.member.Member;
import org.springframework.stereotype.Component;

/**
 * 공통 매퍼 유틸리티 클래스
 * 
 * <p>BaseEntity에서 BaseDto로 감사 정보를 매핑하는 공통 메서드를 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-09-02
 * @version 1.0
 */
@Component
public class AuditMapper {

    /**
     * BaseEntity에서 BaseDto로 감사 정보를 매핑합니다.
     * 
     * @param entity 소스 엔티티
     * @param dto 대상 DTO
     */
    public void mapAuditFields(BaseEntity entity, BaseDto dto) {
        if (entity == null || dto == null) {
            return;
        }

        // 시간 정보 매핑
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setDeleted(entity.getDeleted());

        // 생성자 정보 매핑
        Member createdBy = entity.getCreatedBy();
        if (createdBy != null) {
            dto.setCreatedBy(createdBy.getEmail());
            dto.setCreatedByNickname(createdBy.getNickname());
        }

        // 수정자 정보 매핑
        Member updatedBy = entity.getUpdatedBy();
        if (updatedBy != null) {
            dto.setUpdatedBy(updatedBy.getEmail());
            dto.setUpdatedByNickname(updatedBy.getNickname());
        }
    }

    /**
     * BaseEntity에서 AuditDto로 감사 정보를 매핑합니다.
     * 
     * @param entity 소스 엔티티
     * @param auditDto 대상 AuditDto
     */
    public void mapAuditFields(BaseEntity entity, AuditDto auditDto) {
        if (entity == null || auditDto == null) {
            return;
        }

        // 시간 정보 매핑
        auditDto.setCreatedAt(entity.getCreatedAt());
        auditDto.setUpdatedAt(entity.getUpdatedAt());
        auditDto.setDeleted(entity.getDeleted());

        // 생성자 정보 매핑
        Member createdBy = entity.getCreatedBy();
        if (createdBy != null) {
            auditDto.setCreatedBy(createdBy.getEmail());
            auditDto.setCreatedByNickname(createdBy.getNickname());
        }

        // 수정자 정보 매핑
        Member updatedBy = entity.getUpdatedBy();
        if (updatedBy != null) {
            auditDto.setUpdatedBy(updatedBy.getEmail());
            auditDto.setUpdatedByNickname(updatedBy.getNickname());
        }
    }
}
