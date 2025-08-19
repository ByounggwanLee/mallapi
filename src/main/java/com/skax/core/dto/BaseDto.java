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
}
