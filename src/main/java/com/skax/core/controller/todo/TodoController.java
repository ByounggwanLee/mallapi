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
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Tag(name = "Todos", description = "할 일 API - Todo 작업 관리")
public class TodoController {

    private final TodoService todoService;

    /**
     * 할일 생성
     * 
     * @param request 할일 생성 요청 데이터
     * @return 생성된 할일 정보
     */
    @PostMapping
    @Operation(
        summary = "할일 생성", 
        description = "새로운 할일을 생성합니다.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "할일 생성 성공",
            content = @Content(
                schema = @Schema(implementation = TodoResponse.class),
                examples = @ExampleObject(
                    name = "할일 생성 성공 예제",
                    value = """
                    {
                      "success": true,
                      "message": "할일이 성공적으로 생성되었습니다.",
                      "data": {
                        "tno": 1,
                        "title": "SpringBoot 학습하기",
                        "writer": "홍길동",
                        "complete": false,
                        "createdAt": "2025-08-21T10:30:00",
                        "updatedAt": "2025-08-21T10:30:00"
                      },
                      "timestamp": "2025-08-21T10:30:00",
                      "statusCode": 201,
                      "statusText": "Created"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "잘못된 요청 데이터",
            content = @Content(
                examples = @ExampleObject(
                    name = "유효성 검증 실패",
                    value = """
                    {
                      "success": false,
                      "message": "입력값 검증에 실패했습니다",
                      "error": {
                        "hscode": "BAD_REQUEST",
                        "code": "V001",
                        "message": "입력값이 올바르지 않습니다",
                        "fieldErrors": [
                          {
                            "field": "title",
                            "rejectedValue": "",
                            "message": "제목은 필수입니다"
                          }
                        ]
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<TodoResponse> createTodo(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "할일 생성 요청 데이터",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "할일 생성 요청 예제",
                        value = """
                        {
                          "title": "SpringBoot 학습하기",
                          "writer": "홍길동"
                        }
                        """
                    )
                )
            )
            TodoCreateRequest request) {
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
    @Operation(
        summary = "할일 조회", 
        description = "할일 번호로 특정 할일을 조회합니다.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "할일 조회 성공",
            content = @Content(
                schema = @Schema(implementation = TodoResponse.class),
                examples = @ExampleObject(
                    name = "할일 조회 성공 예제",
                    value = """
                    {
                      "success": true,
                      "message": "할일을 성공적으로 조회했습니다.",
                      "data": {
                        "tno": 1,
                        "title": "SpringBoot 학습하기",
                        "writer": "홍길동",
                        "complete": false,
                        "createdAt": "2025-08-21T10:30:00",
                        "updatedAt": "2025-08-21T10:30:00"
                      },
                      "timestamp": "2025-08-21T10:30:00",
                      "statusCode": 200,
                      "statusText": "OK"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "할일을 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(
                    name = "할일 없음 예제",
                    value = """
                    {
                      "success": false,
                      "message": "할일을 찾을 수 없습니다",
                      "error": {
                        "hscode": "NOT_FOUND",
                        "code": "T001",
                        "message": "할일을 찾을 수 없습니다",
                        "details": "ID 999에 해당하는 할일이 존재하지 않습니다",
                        "timestamp": "2025-08-21T10:30:00",
                        "path": "/api/v1/todos/999"
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<TodoResponse> getTodo(
            @Parameter(description = "할일 번호", example = "1", required = true)
            @PathVariable Long tno) {
        log.info("할일 조회 요청 - 번호: {}", tno);
        
        TodoResponse response = todoService.getTodoByTno(tno);
        return AxResponseEntity.ok(response, "할일을 성공적으로 조회했습니다.");
    }

    /**
     * 할일 목록 조회 (페이징)
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 할일 목록
     */
    @GetMapping
    @Operation(summary = "할일 목록 조회", description = "페이징된 할일 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> getTodos(
            @PageableDefault(size = 20, sort = "tno", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("할일 목록 조회 요청 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        PageResponse<TodoResponse> response = todoService.getAllTodos(pageable);
        return AxResponseEntity.ok(response, "할일 목록을 성공적으로 조회했습니다.");
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
            @PathVariable Long tno,
            @Valid @RequestBody TodoUpdateRequest request) {
        log.info("할일 수정 요청 - 번호: {}, 제목: {}", tno, request.getTitle());
        
        TodoResponse response = todoService.updateTodo(tno, request);
        return AxResponseEntity.updated(response, "할일이 성공적으로 수정되었습니다.");
    }

    /**
     * 할일 삭제
     * 
     * @param tno 할일 번호
     * @return 삭제 결과
     */
    @DeleteMapping("/{tno}")
    @Operation(summary = "할일 삭제", description = "기존 할일을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "할일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Void> deleteTodo(@PathVariable Long tno) {
        log.info("할일 삭제 요청 - 번호: {}", tno);
        
        todoService.deleteTodo(tno);
        return AxResponseEntity.deleted("할일이 성공적으로 삭제되었습니다.");
    }

    /**
     * 할일 완료 상태 토글
     * 
     * @param tno 할일 번호
     * @return 수정된 할일 정보
     */
    @PatchMapping("/{tno}/toggle")
    @Operation(summary = "할일 완료 상태 토글", description = "할일의 완료 상태를 변경합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 상태 변경 성공"),
        @ApiResponse(responseCode = "404", description = "할일을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<TodoResponse> toggleTodoComplete(@PathVariable Long tno) {
        log.info("할일 완료 상태 토글 요청 - 번호: {}", tno);
        
        TodoResponse response = todoService.toggleTodoComplete(tno);
        return AxResponseEntity.ok(response, "할일 상태가 성공적으로 변경되었습니다.");
    }

    /**
     * 작성자별 할일 목록 조회
     * 
     * @param writer 작성자명
     * @param pageable 페이징 정보
     * @return 작성자별 할일 목록
     */
    @GetMapping("/writer/{writer}")
    @Operation(summary = "작성자별 할일 조회", description = "특정 작성자의 할일 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작성자별 할일 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> getTodosByWriter(
            @PathVariable String writer,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("작성자별 할일 조회 요청 - 작성자: {}", writer);
        
        PageResponse<TodoResponse> response = todoService.getTodosByWriter(writer, pageable);
        return AxResponseEntity.ok(response, "작성자별 할일 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 완료 상태별 할일 목록 조회
     * 
     * @param complete 완료 상태
     * @param pageable 페이징 정보
     * @return 완료 상태별 할일 목록
     */
    @GetMapping("/status/{complete}")
    @Operation(summary = "완료 상태별 할일 조회", description = "완료/미완료 상태에 따른 할일 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "완료 상태별 할일 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> getTodosByComplete(
            @PathVariable Boolean complete,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("완료 상태별 할일 조회 요청 - 완료여부: {}", complete);
        
        PageResponse<TodoResponse> response = todoService.getTodosByComplete(complete, pageable);
        return AxResponseEntity.ok(response, "완료 상태별 할일 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 작성자와 완료 상태로 할일 검색
     * 
     * @param writer 작성자명
     * @param complete 완료 상태
     * @param pageable 페이징 정보
     * @return 검색 결과
     */
    @GetMapping("/search")
    @Operation(summary = "할일 검색", description = "작성자와 완료 상태로 할일을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 검색 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> searchTodos(
            @RequestParam(required = false) String writer,
            @RequestParam(required = false) Boolean complete,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("할일 검색 요청 - 작성자: {}, 완료여부: {}", writer, complete);
        
        PageResponse<TodoResponse> response = todoService.getTodosByWriterAndComplete(writer, complete, pageable);
        return AxResponseEntity.ok(response, "할일 검색을 성공적으로 완료했습니다.");
    }

    /**
     * 모든 할일 개수 조회
     * 
     * @return 할일 총 개수
     */
    @GetMapping("/count")
    @Operation(summary = "할일 개수 조회", description = "등록된 모든 할일의 개수를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 개수 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Long> getTodoCount() {
        log.info("할일 개수 조회 요청");
        
        Long count = todoService.getTodoCount();
        return AxResponseEntity.ok(count, "할일 개수를 성공적으로 조회했습니다.");
    }

    /**
     * 작성자별 할일 개수 조회
     * 
     * @param writer 작성자명
     * @return 작성자별 할일 개수
     */
    @GetMapping("/count/writer/{writer}")
    @Operation(summary = "작성자별 할일 개수 조회", description = "특정 작성자의 할일 개수를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작성자별 할일 개수 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Long> getTodoCountByWriter(@PathVariable String writer) {
        log.info("작성자별 할일 개수 조회 요청 - 작성자: {}", writer);
        
        Long count = todoService.getTodoCountByWriter(writer);
        return AxResponseEntity.ok(count, "작성자별 할일 개수를 성공적으로 조회했습니다.");
    }

    /**
     * 완료 상태별 할일 개수 조회
     * 
     * @param complete 완료 상태
     * @return 완료 상태별 할일 개수
     */
    @GetMapping("/count/status/{complete}")
    @Operation(summary = "완료 상태별 할일 개수 조회", description = "완료/미완료 상태별 할일 개수를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "완료 상태별 할일 개수 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Long> getTodoCountByComplete(@PathVariable Boolean complete) {
        log.info("완료 상태별 할일 개수 조회 요청 - 완료여부: {}", complete);
        
        Long count = todoService.getTodoCountByComplete(complete);
        return AxResponseEntity.ok(count, "완료 상태별 할일 개수를 성공적으로 조회했습니다.");
    }

    /**
     * 할일 제목으로 검색
     * 
     * @param title 검색할 제목 (부분 일치)
     * @param pageable 페이징 정보
     * @return 제목으로 검색된 할일 목록
     */
    @GetMapping("/search/title")
    @Operation(summary = "제목으로 할일 검색", description = "제목에 포함된 키워드로 할일을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "제목 검색 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<PageResponse<TodoResponse>> searchTodosByTitle(
            @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("제목으로 할일 검색 요청 - 제목: {}", title);
        
        PageResponse<TodoResponse> response = todoService.searchTodosByTitle(title, pageable);
        return AxResponseEntity.ok(response, "제목 검색을 성공적으로 완료했습니다.");
    }

    /**
     * 모든 할일 삭제 (관리자용)
     * 
     * @return 삭제 결과
     */
    @DeleteMapping("/all")
    @Operation(summary = "모든 할일 삭제", description = "등록된 모든 할일을 삭제합니다. (관리자 전용)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "모든 할일 삭제 성공"),
        @ApiResponse(responseCode = "403", description = "권한 부족"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Void> deleteAllTodos() {
        log.info("모든 할일 삭제 요청");
        
        todoService.deleteAllTodos();
        return AxResponseEntity.deleted("모든 할일이 성공적으로 삭제되었습니다.");
    }

    /**
     * 작성자별 모든 할일 삭제
     * 
     * @param writer 작성자명
     * @return 삭제 결과
     */
    @DeleteMapping("/writer/{writer}")
    @Operation(summary = "작성자별 할일 삭제", description = "특정 작성자의 모든 할일을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "작성자별 할일 삭제 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<Void> deleteTodosByWriter(@PathVariable String writer) {
        log.info("작성자별 할일 삭제 요청 - 작성자: {}", writer);
        
        todoService.deleteTodosByWriter(writer);
        return AxResponseEntity.deleted("작성자별 할일이 성공적으로 삭제되었습니다.");
    }

    /**
     * 할일 목록을 리스트 형태로 조회 (페이징 없음)
     * 
     * @return 모든 할일 목록
     */
    @GetMapping("/list")
    @Operation(summary = "할일 전체 목록 조회", description = "페이징 없이 모든 할일 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "할일 전체 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public AxResponseEntity<List<TodoResponse>> getAllTodos() {
        log.info("할일 전체 목록 조회 요청");
        
        List<TodoResponse> response = todoService.getAllTodos();
        return AxResponseEntity.ok(response, "할일 전체 목록을 성공적으로 조회했습니다.");
    }
}
