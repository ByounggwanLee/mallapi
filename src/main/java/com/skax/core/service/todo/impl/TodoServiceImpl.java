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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        
        // 정렬 파라미터 검증 및 정리
        Pageable validatedPageable = validateAndFixPageable(pageable);
        
        Page<Todo> todoPage = todoRepository.findAll(validatedPageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> getTodosByWriter(String writer, Pageable pageable) {
        log.info("작성자별 할일 목록 조회 - 작성자: {}", writer);
        
        // 정렬 파라미터 검증 및 정리
        Pageable validatedPageable = validateAndFixPageable(pageable);
        
        Page<Todo> todoPage = todoRepository.findByWriter(writer, validatedPageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> getTodosByComplete(Boolean complete, Pageable pageable) {
        log.info("완료 상태별 할일 목록 조회 - 완료여부: {}", complete);
        
        // 정렬 파라미터 검증 및 정리
        Pageable validatedPageable = validateAndFixPageable(pageable);
        
        Page<Todo> todoPage = todoRepository.findByComplete(complete, validatedPageable);
        Page<TodoResponse> responsePage = todoPage.map(todoMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<TodoResponse> getTodosByWriterAndComplete(String writer, Boolean complete, Pageable pageable) {
        log.info("작성자와 완료 상태별 할일 목록 조회 - 작성자: {}, 완료여부: {}", writer, complete);
        
        // 정렬 파라미터 검증 및 정리
        Pageable validatedPageable = validateAndFixPageable(pageable);
        
        Page<Todo> todoPage = todoRepository.findByWriterAndComplete(writer, complete, validatedPageable);
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

    /**
     * Pageable 객체의 정렬 파라미터를 검증하고 안전한 정렬을 적용합니다.
     * 
     * @param pageable 원본 Pageable 객체
     * @return 검증된 Pageable 객체
     */
    private Pageable validateAndFixPageable(Pageable pageable) {
        // 허용되는 정렬 필드 목록
        final String[] ALLOWED_SORT_FIELDS = {"tno", "title", "writer", "complete", "createdAt", "updatedAt"};
        
        Sort validatedSort = Sort.by("tno").descending(); // 기본 정렬
        
        if (pageable.getSort().isSorted()) {
            List<Sort.Order> validOrders = new ArrayList<>();
            
            log.debug("정렬 파라미터 디버깅 - pageable.getSort(): {}", pageable.getSort());
            log.debug("정렬 파라미터 디버깅 - pageable.getSort().toString(): {}", pageable.getSort().toString());
            
            for (Sort.Order order : pageable.getSort()) {
                String property = order.getProperty();
                
                log.debug("정렬 필드 검증 - property: '{}', direction: {}", property, order.getDirection());
                
                // 잘못된 JSON 형태의 정렬 파라미터 정리
                String cleanProperty = cleanSortProperty(property);
                log.debug("정리된 정렬 필드: '{}' -> '{}'", property, cleanProperty);
                
                // 허용된 필드인지 확인
                if (java.util.Arrays.asList(ALLOWED_SORT_FIELDS).contains(cleanProperty)) {
                    // 정리된 프로퍼티로 새로운 Order 생성
                    Sort.Order cleanOrder = new Sort.Order(order.getDirection(), cleanProperty);
                    validOrders.add(cleanOrder);
                    log.debug("유효한 정렬 필드 적용: {} {}", cleanProperty, order.getDirection());
                } else {
                    log.warn("허용되지 않은 정렬 필드 무시: '{}' (원본: '{}')", cleanProperty, property);
                }
            }
            
            if (!validOrders.isEmpty()) {
                validatedSort = Sort.by(validOrders);
            }
        }
        
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                validatedSort
        );
    }

    /**
     * 잘못된 형태의 정렬 파라미터를 정리합니다.
     * Swagger UI에서 전송되는 JSON 형태의 파라미터를 올바른 필드명으로 변환합니다.
     * 
     * @param property 원본 정렬 프로퍼티 (예: '["string"', '"title"]')
     * @return 정리된 정렬 프로퍼티 (예: 'title')
     */
    private String cleanSortProperty(String property) {
        if (property == null || property.trim().isEmpty()) {
            return "";
        }
        
        String original = property;
        String cleaned = property.trim();
        
        // Swagger UI에서 오는 잘못된 JSON 형태 패턴들 처리
        // 예: '["string"' -> 'string', '"title"]' -> 'title'
        
        // 1. 대괄호와 따옴표 조합 제거
        cleaned = cleaned.replaceAll("^\\[\"", "")           // 시작 [" 제거
                        .replaceAll("\"\\]$", "")            // 끝 "] 제거
                        .replaceAll("^\"|\"$", "")           // 시작/끝 따옴표 제거
                        .replaceAll("^\\[|\\]$", "")         // 시작/끝 대괄호 제거
                        
        // 2. JSON 객체 형태 패턴 제거 (예: "string": ASC에서 string 추출)
                        .replaceAll("\"\\s*:\\s*[A-Z]+", "") // ": ASC" 또는 ": DESC" 패턴 제거
                        .replaceAll(":\\s*[A-Z]+", "")       // ": ASC" 패턴 제거
                        
        // 3. 쉼표와 기타 특수문자 제거
                        .replaceAll(",$", "")                // 끝 쉼표 제거
                        .replaceAll("^,", "")                // 시작 쉼표 제거
                        
        // 4. 최종 정리
                        .trim();
        
        // 빈 문자열이거나 특수문자만 있는 경우 처리
        if (cleaned.isEmpty() || cleaned.matches("[^a-zA-Z0-9_]+")) {
            log.warn("정렬 파라미터 정리 실패 - 원본: '{}', 정리 후: '{}'", original, cleaned);
            return "";
        }
        
        // 유효한 필드명만 허용 (영문자, 숫자, 언더스코어)
        cleaned = cleaned.replaceAll("[^a-zA-Z0-9_]", "");
        
        log.debug("정렬 파라미터 정리 완료 - 원본: '{}' -> 정리: '{}'", original, cleaned);
        return cleaned;
    }
}
