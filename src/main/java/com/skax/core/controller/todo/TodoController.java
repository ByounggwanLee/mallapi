package com.skax.core.controller.todo;

import com.skax.core.common.response.AxResponseEntity;
import com.skax.core.common.response.PageResponse;
import com.skax.core.dto.todo.request.TodoCreateRequest;
import com.skax.core.dto.todo.request.TodoUpdateRequest;
import com.skax.core.dto.todo.response.TodoResponse;
import com.skax.core.service.todo.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 할일 관리 REST API 컨트롤러
 * 
 * <p>할일의 CRUD 및 검색 기능을 제공하는 RESTful API 엔드포인트를 정의합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
@Tag(name = "Todo", description = "할일 관리 API")
public class TodoController {

    private final TodoService todoService;

    /**
     * 할일 생성
     * 
     * @param request 할일 생성 요청 데이터
     * @return 생성된 할일 정보
     */
    @PostMapping
    @Operation(summary = "할일 생성", description = "새로운 할일을 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "할일 생성 성공",
                content = @Content(schema = @Schema(implementation = TodoResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<TodoResponse> createTodo(
            @Valid @RequestBody TodoCreateRequest request) {
        log.info("할일 생성 요청 - 제목: {}, 작성자: {}", request.getTitle(), request.getWriter());
        
        TodoResponse response = todoService.createTodo(request);
        return AxResponseEntity.created(response, "할일이 성공적으로 생성되었습니다.");
    }

    /**
     * 할일 단일 조회
     * 
     * @param tno 할일 번호
     * @return 할일 정보
     */
    @GetMapping("/{tno}")
    @Operation(summary = "할일 조회", description = "할일 번호로 특정 할일을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 조회 성공"),
        @ApiResponse(responseCode = "404", description = "할일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<TodoResponse> getTodo(
            @Parameter(description = "할일 번호") @PathVariable Long tno) {
        log.info("할일 조회 요청 - 할일번호: {}", tno);
        
        TodoResponse response = todoService.getTodoByTno(tno);
        return AxResponseEntity.ok(response, "할일 정보를 성공적으로 조회했습니다.");
    }

    /**
     * 할일 수정
     * 
     * @param tno 할일 번호
     * @param request 할일 수정 요청 데이터
     * @return 수정된 할일 정보
     */
    @PutMapping("/{tno}")
    @Operation(summary = "할일 수정", description = "기존 할일 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "할일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<TodoResponse> updateTodo(
            @Parameter(description = "할일 번호") @PathVariable Long tno,
            @Valid @RequestBody TodoUpdateRequest request) {
        log.info("할일 수정 요청 - 할일번호: {}", tno);
        
        TodoResponse response = todoService.updateTodo(tno, request);
        return AxResponseEntity.updated(response, "할일이 성공적으로 수정되었습니다.");
    }

    /**
     * 할일 삭제
     * 
     * @param tno 할일 번호
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{tno}")
    @Operation(summary = "할일 삭제", description = "기존 할일을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "할일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Void> deleteTodo(
            @Parameter(description = "할일 번호") @PathVariable Long tno) {
        log.info("할일 삭제 요청 - 할일번호: {}", tno);
        
        todoService.deleteTodo(tno);
        return AxResponseEntity.deleted("할일이 성공적으로 삭제되었습니다.");
    }

    /**
     * 전체 할일 목록 조회 (페이징)
     * 
     * @param pageable 페이징 정보
     * @return 할일 목록
     */
    @GetMapping
    @Operation(summary = "전체 할일 목록 조회", description = "전체 할일 목록을 페이징하여 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> getAllTodos(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("전체 할일 목록 조회 요청 - 페이지: {}, 크기: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        PageResponse<TodoResponse> response = todoService.getAllTodos(pageable);
        return AxResponseEntity.okPage(response, "할일 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 작성자별 할일 목록 조회
     * 
     * @param writer 작성자
     * @param pageable 페이징 정보
     * @return 작성자별 할일 목록
     */
    @GetMapping("/writer/{writer}")
    @Operation(summary = "작성자별 할일 목록 조회", description = "특정 작성자의 할일 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작성자별 할일 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> getTodosByWriter(
            @Parameter(description = "작성자") @PathVariable String writer,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("작성자별 할일 목록 조회 요청 - 작성자: {}", writer);
        
        PageResponse<TodoResponse> response = todoService.getTodosByWriter(writer, pageable);
        return AxResponseEntity.okPage(response, "작성자별 할일 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 완료 상태별 할일 목록 조회
     * 
     * @param complete 완료 상태
     * @param pageable 페이징 정보
     * @return 완료 상태별 할일 목록
     */
    @GetMapping("/complete/{complete}")
    @Operation(summary = "완료 상태별 할일 목록 조회", description = "완료 상태에 따른 할일 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "완료 상태별 할일 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> getTodosByComplete(
            @Parameter(description = "완료 상태") @PathVariable Boolean complete,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("완료 상태별 할일 목록 조회 요청 - 완료여부: {}", complete);
        
        PageResponse<TodoResponse> response = todoService.getTodosByComplete(complete, pageable);
        return AxResponseEntity.okPage(response, "완료 상태별 할일 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 제목으로 할일 검색
     * 
     * @param title 검색할 제목
     * @param pageable 페이징 정보
     * @return 검색된 할일 목록
     */
    @GetMapping("/search")
    @Operation(summary = "제목으로 할일 검색", description = "제목에 포함된 키워드로 할일을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 검색 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> searchTodosByTitle(
            @Parameter(description = "검색할 제목") @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("제목으로 할일 검색 요청 - 키워드: {}", title);
        
        PageResponse<TodoResponse> response = todoService.searchTodosByTitle(title, pageable);
        return AxResponseEntity.okPage(response, "할일 검색을 성공적으로 완료했습니다.");
    }

    /**
     * 고급 검색 (제목, 작성자)
     * 
     * @param title 검색할 제목 (선택사항)
     * @param writer 검색할 작성자 (선택사항)
     * @param pageable 페이징 정보
     * @return 검색된 할일 목록
     */
    @GetMapping("/search/advanced")
    @Operation(summary = "고급 할일 검색", description = "제목과 작성자로 할일을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "고급 할일 검색 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> searchTodos(
            @Parameter(description = "검색할 제목") @RequestParam(required = false) String title,
            @Parameter(description = "검색할 작성자") @RequestParam(required = false) String writer,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("고급 할일 검색 요청 - 제목: {}, 작성자: {}", title, writer);
        
        PageResponse<TodoResponse> response = todoService.searchTodos(title, writer, pageable);
        return AxResponseEntity.okPage(response, "고급 할일 검색을 성공적으로 완료했습니다.");
    }

    /**
     * 할일 완료 상태 토글
     * 
     * @param tno 할일 번호
     * @return 수정된 할일 정보
     */
    @PatchMapping("/{tno}/toggle")
    @Operation(summary = "할일 완료 상태 토글", description = "할일의 완료 상태를 토글합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 완료 상태 토글 성공"),
        @ApiResponse(responseCode = "404", description = "할일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<TodoResponse> toggleTodoComplete(
            @Parameter(description = "할일 번호") @PathVariable Long tno) {
        log.info("할일 완료 상태 토글 요청 - 할일번호: {}", tno);
        
        TodoResponse response = todoService.toggleTodoComplete(tno);
        return AxResponseEntity.updated(response, "할일 완료 상태가 성공적으로 변경되었습니다.");
    }

    /**
     * 작성자별 완료된 할일 개수 조회
     * 
     * @param writer 작성자
     * @return 완료된 할일 개수
     */
    @GetMapping("/stats/completed/{writer}")
    @Operation(summary = "작성자별 완료된 할일 개수 조회", description = "특정 작성자의 완료된 할일 개수를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "완료된 할일 개수 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Long> getCompletedCountByWriter(
            @Parameter(description = "작성자") @PathVariable String writer) {
        log.info("작성자별 완료된 할일 개수 조회 요청 - 작성자: {}", writer);
        
        long count = todoService.getCompletedCountByWriter(writer);
        return AxResponseEntity.ok(count, "완료된 할일 개수를 성공적으로 조회했습니다.");
    }

    /**
     * 작성자별 미완료된 할일 개수 조회
     * 
     * @param writer 작성자
     * @return 미완료된 할일 개수
     */
    @GetMapping("/stats/incomplete/{writer}")
    @Operation(summary = "작성자별 미완료된 할일 개수 조회", description = "특정 작성자의 미완료된 할일 개수를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "미완료된 할일 개수 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Long> getIncompleteCountByWriter(
            @Parameter(description = "작성자") @PathVariable String writer) {
        log.info("작성자별 미완료된 할일 개수 조회 요청 - 작성자: {}", writer);
        
        long count = todoService.getIncompleteCountByWriter(writer);
        return AxResponseEntity.ok(count, "미완료된 할일 개수를 성공적으로 조회했습니다.");
    }

    /**
     * 작성자의 모든 할일 목록 조회 (페이징 없음)
     * 
     * @param writer 작성자
     * @return 작성자의 모든 할일 목록
     */
    @GetMapping("/all/{writer}")
    @Operation(summary = "작성자의 모든 할일 조회", description = "특정 작성자의 모든 할일을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작성자의 모든 할일 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<List<TodoResponse>> getAllTodosByWriter(
            @Parameter(description = "작성자") @PathVariable String writer) {
        log.info("작성자의 모든 할일 조회 요청 - 작성자: {}", writer);
        
        List<TodoResponse> response = todoService.getAllTodosByWriter(writer);
        return AxResponseEntity.ok(response, "작성자의 모든 할일을 성공적으로 조회했습니다.");
    }

    /**
     * 최근 완료된 할일 목록 조회
     * 
     * @param pageable 페이징 정보
     * @return 최근 완료된 할일 목록
     */
    @GetMapping("/recent/completed")
    @Operation(summary = "최근 완료된 할일 목록 조회", description = "최근에 완료된 할일 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "최근 완료된 할일 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> getRecentlyCompleted(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("최근 완료된 할일 목록 조회 요청");
        
        PageResponse<TodoResponse> response = todoService.getRecentlyCompleted(pageable);
        return AxResponseEntity.okPage(response, "최근 완료된 할일 목록을 성공적으로 조회했습니다.");
    }
}
