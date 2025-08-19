package com.skax.core.dto.todo.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 할일 수정 요청 DTO
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
public class TodoUpdateRequest {

    /**
     * 제목
     */
    @Size(max = 256, message = "제목은 256자 이하여야 합니다.")
    private String title;

    /**
     * 작성자
     */
    @Size(max = 256, message = "작성자는 256자 이하여야 합니다.")
    private String writer;

    /**
     * 완료 여부
     */
    private Boolean complete;
}
