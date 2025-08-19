package com.skax.core.dto.todo.response;

import com.skax.core.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 할일 정보 응답 DTO
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoResponse extends BaseDto {

    /**
     * 할일 번호 (PK)
     */
    private Long tno;

    /**
     * 제목
     */
    private String title;

    /**
     * 작성자
     */
    private String writer;

    /**
     * 완료 여부
     */
    private Boolean complete;

    /**
     * 완료 상태 문자열
     */
    public String getCompleteStatus() {
        return Boolean.TRUE.equals(complete) ? "완료" : "미완료";
    }
}
