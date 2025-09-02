package com.skax.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 모든 DTO의 공통 속성을 정의하는 기본 클래스
 * 
 * <p>이 클래스는 모든 DTO에서 공통으로 사용되는 필드들을 정의합니다:</p>
 * <ul>
 *   <li>생성일시 (createdAt)</li>
 *   <li>수정일시 (updatedAt)</li>
 *   <li>생성자 정보 (createdBy, createdByNickname)</li>
 *   <li>수정자 정보 (updatedBy, updatedByNickname)</li>
 *   <li>삭제 여부 (deleted)</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Getter
@Setter
public abstract class BaseDto {

    /**
     * 생성일시
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

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
     * 삭제 여부
     */
    private Boolean deleted;
}
