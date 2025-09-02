package com.skax.core.dto.todo.mapper;

import com.skax.core.dto.todo.request.TodoCreateRequest;
import com.skax.core.dto.todo.request.TodoUpdateRequest;
import com.skax.core.dto.todo.response.TodoResponse;
import com.skax.core.entity.todo.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Todo 엔티티와 DTO 간의 매핑을 담당하는 MapStruct 매퍼
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TodoMapper {

    /**
     * TodoCreateRequest를 Todo 엔티티로 변환
     * 
     * @param request 할일 생성 요청 DTO
     * @return Todo 엔티티
     */
    @Mapping(target = "tno", ignore = true)
    @Mapping(target = "dueDate", ignore = true)
    Todo toEntity(TodoCreateRequest request);

    /**
     * Todo 엔티티를 TodoResponse로 변환
     * 
     * @param todo Todo 엔티티
     * @return 할일 응답 DTO
     */
    TodoResponse toResponse(Todo todo);

    /**
     * TodoUpdateRequest의 정보로 기존 Todo 엔티티를 업데이트
     * 
     * @param request 할일 수정 요청 DTO
     * @param todo 수정할 Todo 엔티티
     */
    @Mapping(target = "tno", ignore = true)
    @Mapping(target = "dueDate", ignore = true)
    void updateEntity(TodoUpdateRequest request, @MappingTarget Todo todo);
}
