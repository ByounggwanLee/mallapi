package com.skax.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 감사 정보를 나타내는 공통 DTO 클래스
 * 
 * <p>엔티티의 생성자, 수정자, 생성일시, 수정일시 정보를 포함합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-09-02
 * @version 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditDto {

    /**
     * 생성자 이메일
     */
    private String createdBy;

    /**
     * 생성자 닉네임
     */
    private String createdByNickname;

    /**
     * 최종 수정자 이메일
     */
    private String updatedBy;

    /**
     * 최종 수정자 닉네임
     */
    private String updatedByNickname;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    /**
     * 삭제 여부
     */
    private Boolean deleted;
}
