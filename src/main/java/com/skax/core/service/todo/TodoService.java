package com.skax.core.service.todo;

import com.skax.core.dto.todo.request.TodoCreateRequest;
import com.skax.core.dto.todo.request.TodoUpdateRequest;
import com.skax.core.dto.todo.response.TodoResponse;
import com.skax.core.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Todo 관리 서비스 인터페이스
 * 
 * <p>Todo의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 정의합니다.</p>
 * 
 * <p>주요 기능:</p>
 * <ul>
 *   <li>Todo 등록, 수정, 삭제 (논리적 삭제)</li>
 *   <li>Todo 목록 조회 (페이징)</li>
 *   <li>제목, 내용 기반 검색</li>
 *   <li>완료 상태별 조회</li>
 *   <li>날짜별 조회</li>
 *   <li>Todo 통계 정보</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
public interface TodoService {

    /**
     * 새로운 할일을 생성합니다.
     * 
     * @param request 할일 생성 요청 정보
     * @return 생성된 할일 정보
     */
    TodoResponse createTodo(TodoCreateRequest request);

    /**
     * 할일 번호로 할일을 조회합니다.
     * 
     * @param tno 할일 번호
     * @return 할일 정보
     */
    TodoResponse getTodoByTno(Long tno);

    /**
     * 할일 정보를 수정합니다.
     * 
     * @param tno 할일 번호
     * @param request 수정할 할일 정보
     * @return 수정된 할일 정보
     */
    TodoResponse updateTodo(Long tno, TodoUpdateRequest request);

    /**
     * 할일을 삭제합니다.
     * 
     * @param tno 할일 번호
     */
    void deleteTodo(Long tno);

    /**
     * 모든 할일 목록을 페이징으로 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    PageResponse<TodoResponse> getAllTodos(Pageable pageable);

    /**
     * 작성자별 할일 목록을 조회합니다.
     * 
     * @param writer 작성자
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    PageResponse<TodoResponse> getTodosByWriter(String writer, Pageable pageable);

    /**
     * 완료 상태별 할일 목록을 조회합니다.
     * 
     * @param complete 완료 여부
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    PageResponse<TodoResponse> getTodosByComplete(Boolean complete, Pageable pageable);

    /**
     * 작성자와 완료 상태로 할일 목록을 조회합니다.
     * 
     * @param writer 작성자
     * @param complete 완료 여부
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    PageResponse<TodoResponse> getTodosByWriterAndComplete(String writer, Boolean complete, Pageable pageable);

    /**
     * 제목으로 할일을 검색합니다.
     * 
     * @param title 제목 키워드
     * @param pageable 페이징 정보
     * @return 검색된 할일 목록
     */
    PageResponse<TodoResponse> searchTodosByTitle(String title, Pageable pageable);

    /**
     * 제목과 작성자로 할일을 검색합니다.
     * 
     * @param title 제목 키워드
     * @param writer 작성자
     * @param pageable 페이징 정보
     * @return 검색된 할일 목록
     */
    PageResponse<TodoResponse> searchTodos(String title, String writer, Pageable pageable);

    /**
     * 할일의 완료 상태를 토글합니다.
     * 
     * @param tno 할일 번호
     * @return 상태 변경된 할일 정보
     */
    TodoResponse toggleTodoComplete(Long tno);

    /**
     * 작성자별 완료된 할일 개수를 조회합니다.
     * 
     * @param writer 작성자
     * @return 완료된 할일 개수
     */
    long getCompletedCountByWriter(String writer);

    /**
     * 작성자별 미완료된 할일 개수를 조회합니다.
     * 
     * @param writer 작성자
     * @return 미완료된 할일 개수
     */
    long getIncompleteCountByWriter(String writer);

    /**
     * 작성자의 모든 할일 목록을 조회합니다.
     * 
     * @param writer 작성자
     * @return 할일 목록
     */
    List<TodoResponse> getAllTodosByWriter(String writer);

    /**
     * 최근 완료된 할일 목록을 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 최근 완료된 할일 목록
     */
    PageResponse<TodoResponse> getRecentlyCompleted(Pageable pageable);

    /**
     * 모든 할일의 총 개수를 조회합니다.
     * 
     * @return 할일 총 개수
     */
    Long getTodoCount();

    /**
     * 작성자별 할일 개수를 조회합니다.
     * 
     * @param writer 작성자
     * @return 작성자별 할일 개수
     */
    Long getTodoCountByWriter(String writer);

    /**
     * 완료 상태별 할일 개수를 조회합니다.
     * 
     * @param complete 완료 상태
     * @return 완료 상태별 할일 개수
     */
    Long getTodoCountByComplete(Boolean complete);

    /**
     * 모든 할일을 삭제합니다.
     */
    void deleteAllTodos();

    /**
     * 작성자별 모든 할일을 삭제합니다.
     * 
     * @param writer 작성자
     */
    void deleteTodosByWriter(String writer);

    /**
     * 모든 할일 목록을 리스트로 조회합니다. (페이징 없음)
     * 
     * @return 모든 할일 목록
     */
    List<TodoResponse> getAllTodos();
}
