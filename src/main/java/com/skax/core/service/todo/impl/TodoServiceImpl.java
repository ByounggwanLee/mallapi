package com.skax.core.service.todo.impl;

import com.skax.core.common.exception.BusinessException;
import com.skax.core.common.response.ErrorCode;
import com.skax.core.common.response.PageResponse;
import com.skax.core.dto.todo.mapper.TodoMapper;
import com.skax.core.dto.todo.request.TodoCreateRequest;
import com.skax.core.dto.todo.request.TodoUpdateRequest;
import com.skax.core.dto.todo.response.TodoResponse;
import com.skax.core.entity.todo.Todo;
import com.skax.core.repository.todo.TodoRepository;
import com.skax.core.service.todo.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 할일 관리 서비스 구현체
 * 
 * <p>할일의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 구현합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    @Override
    @Transactional
    public TodoResponse createTodo(TodoCreateRequest request) {
        log.info("할일 생성 시작 - 제목: {}, 작성자: {}", request.getTitle(), request.getWriter());
        
        Todo todo = todoMapper.toEntity(request);
        Todo savedTodo = todoRepository.save(todo);
        
        log.info("할일 생성 완료 - 할일번호: {}", savedTodo.getTno());
        return todoMapper.toResponse(savedTodo);
    }

    @Override
    public TodoResponse getTodoByTno(Long tno) {
        log.info("할일 조회 시작 - 할일번호: {}", tno);
        
        Todo todo = findTodoByTno(tno);
        return todoMapper.toResponse(todo);
    }

    @Override
    @Transactional
    public TodoResponse updateTodo(Long tno, TodoUpdateRequest request) {
        log.info("할일 수정 시작 - 할일번호: {}", tno);
        
        Todo todo = findTodoByTno(tno);
        todo.updateTodo(request.getTitle(), request.getWriter(), request.getComplete());
        
        Todo updatedTodo = todoRepository.save(todo);
        
        log.info("할일 수정 완료 - 할일번호: {}", updatedTodo.getTno());
        return todoMapper.toResponse(updatedTodo);
    }

    @Override
    @Transactional
    public void deleteTodo(Long tno) {
        log.info("할일 삭제 시작 - 할일번호: {}", tno);
        
        Todo todo = findTodoByTno(tno);
        todoRepository.delete(todo);
        
        log.info("할일 삭제 완료 - 할일번호: {}", tno);
    }

    @Override
    public PageResponse<TodoResponse> getAllTodos(Pageable pageable) {
        log.info("전체 할일 목록 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Todo> todoPage = todoRepository.findAll(pageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> getTodosByWriter(String writer, Pageable pageable) {
        log.info("작성자별 할일 목록 조회 - 작성자: {}", writer);
        
        Page<Todo> todoPage = todoRepository.findByWriter(writer, pageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> getTodosByComplete(Boolean complete, Pageable pageable) {
        log.info("완료 상태별 할일 목록 조회 - 완료여부: {}", complete);
        
        Page<Todo> todoPage = todoRepository.findByComplete(complete, pageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> getTodosByWriterAndComplete(String writer, Boolean complete, Pageable pageable) {
        log.info("작성자와 완료 상태별 할일 목록 조회 - 작성자: {}, 완료여부: {}", writer, complete);
        
        Page<Todo> todoPage = todoRepository.findByWriterAndComplete(writer, complete, pageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> searchTodosByTitle(String title, Pageable pageable) {
        log.info("제목으로 할일 검색 - 키워드: {}", title);
        
        Page<Todo> todoPage = todoRepository.findByTitleContainingIgnoreCase(title, pageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> searchTodos(String title, String writer, Pageable pageable) {
        log.info("제목과 작성자로 할일 검색 - 제목: {}, 작성자: {}", title, writer);
        
        Page<Todo> todoPage = todoRepository.searchTodos(title, writer, pageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public TodoResponse toggleTodoComplete(Long tno) {
        log.info("할일 완료 상태 토글 - 할일번호: {}", tno);
        
        Todo todo = findTodoByTno(tno);
        todo.toggleComplete();
        
        Todo updatedTodo = todoRepository.save(todo);
        
        log.info("할일 완료 상태 토글 완료 - 할일번호: {}, 완료여부: {}", tno, updatedTodo.getComplete());
        return todoMapper.toResponse(updatedTodo);
    }

    @Override
    public long getCompletedCountByWriter(String writer) {
        log.info("작성자별 완료된 할일 개수 조회 - 작성자: {}", writer);
        
        return todoRepository.countCompletedByWriter(writer);
    }

    @Override
    public long getIncompleteCountByWriter(String writer) {
        log.info("작성자별 미완료된 할일 개수 조회 - 작성자: {}", writer);
        
        return todoRepository.countIncompleteByWriter(writer);
    }

    @Override
    public List<TodoResponse> getAllTodosByWriter(String writer) {
        log.info("작성자의 모든 할일 목록 조회 - 작성자: {}", writer);
        
        List<Todo> todos = todoRepository.findByWriterOrderByCreatedAtDesc(writer);
        return todos.stream()
                .map(todoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<TodoResponse> getRecentlyCompleted(Pageable pageable) {
        log.info("최근 완료된 할일 목록 조회");
        
        Page<Todo> todoPage = todoRepository.findRecentlyCompleted(pageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public Long getTodoCount() {
        log.info("모든 할일 개수 조회");
        return todoRepository.count();
    }

    @Override
    public Long getTodoCountByWriter(String writer) {
        log.info("작성자별 할일 개수 조회 - 작성자: {}", writer);
        return todoRepository.countByWriter(writer);
    }

    @Override
    public Long getTodoCountByComplete(Boolean complete) {
        log.info("완료 상태별 할일 개수 조회 - 완료여부: {}", complete);
        return todoRepository.countByComplete(complete);
    }

    @Override
    @Transactional
    public void deleteAllTodos() {
        log.info("모든 할일 삭제");
        todoRepository.deleteAll();
    }

    @Override
    @Transactional
    public void deleteTodosByWriter(String writer) {
        log.info("작성자별 할일 삭제 - 작성자: {}", writer);
        todoRepository.deleteByWriter(writer);
    }

    @Override
    public List<TodoResponse> getAllTodos() {
        log.info("모든 할일 목록 조회 (페이징 없음)");
        return todoRepository.findAll()
                .stream()
                .map(todoMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 할일 번호로 할일을 조회하는 private 메서드
     * 
     * @param tno 할일 번호
     * @return 할일 엔티티
     * @throws BusinessException 할일을 찾을 수 없는 경우
     */
    private Todo findTodoByTno(Long tno) {
        return todoRepository.findByTno(tno)
                .orElseThrow(() -> {
                    log.warn("할일을 찾을 수 없음 - 할일번호: {}", tno);
                    return new BusinessException(ErrorCode.TODO_NOT_FOUND, 
                            String.format("할일번호 %d에 해당하는 할일을 찾을 수 없습니다.", tno));
                });
    }
}
