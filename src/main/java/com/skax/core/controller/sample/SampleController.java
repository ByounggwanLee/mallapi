package com.skax.core.controller.sample;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skax.core.common.response.AxResponseEntity;
import com.skax.core.common.response.ErrorCode;
import com.skax.core.common.response.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 샘플 API 컨트롤러
 * 
 * <p>AxResponseEntity와 응답 형식 표준화를 테스트하기 위한 샘플 컨트롤러입니다.
 * 다양한 응답 패턴과 에러 케이스를 포함하여 구현되었습니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Tag(name = "Samples", description = "샘플 API - 기본 샘플 데이터 관리")
@RestController
@RequestMapping("/api/v1/samples")
@Validated
@Slf4j
public class SampleController {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    // ==================== 성공 응답 예시 ====================

    /**
     * 샘플 목록 조회 (페이징)
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 샘플 목록
     */
    @Operation(summary = "샘플 목록 조회", description = "페이징을 지원하는 샘플 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "샘플 목록 조회 성공")
    })
    @GetMapping
    public AxResponseEntity<PageResponse<SampleResponse>> getSamples(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Getting samples with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        
        // 샘플 데이터 생성
        List<SampleResponse> samples = IntStream.range(0, 50)
                .mapToObj(i -> SampleResponse.builder()
                        .id((long) (i + 1))
                        .name("샘플 " + (i + 1))
                        .description("샘플 설명 " + (i + 1))
                        .status("ACTIVE")
                        .createdAt(LocalDateTime.now().minusDays(i))
                        .build())
                .toList();
        
        // 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), samples.size());
        List<SampleResponse> pageContent = samples.subList(start, end);
        
        Page<SampleResponse> samplePage = new PageImpl<>(pageContent, pageable, samples.size());
        PageResponse<SampleResponse> pageResponse = PageResponse.from(samplePage);
        
        return AxResponseEntity.okPage(pageResponse, "샘플 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 단일 샘플 조회
     * 
     * @param id 샘플 ID
     * @return 샘플 정보
     */
    @Operation(summary = "샘플 조회", description = "ID를 통해 특정 샘플을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "샘플 조회 성공"),
        @ApiResponse(responseCode = "404", description = "샘플을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public AxResponseEntity<SampleResponse> getSample(@PathVariable @Positive Long id) {
        log.info("Getting sample with id: {}", id);
        
        // 샘플이 없는 경우 에러 응답
        if (id > 100) {
            return AxResponseEntity.notFound(ErrorCode.SAMPLE_NOT_FOUND);
        }
        
        SampleResponse sample = SampleResponse.builder()
                .id(id)
                .name("샘플 " + id)
                .description("샘플 설명 " + id)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        
        return AxResponseEntity.ok(sample, "샘플 정보를 성공적으로 조회했습니다.");
    }

    /**
     * 샘플 생성
     * 
     * @param request 샘플 생성 요청
     * @return 생성된 샘플 정보
     */
    @Operation(summary = "샘플 생성", description = "새로운 샘플을 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "샘플 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "중복된 샘플명")
    })
    @PostMapping
    public AxResponseEntity<SampleResponse> createSample(@Valid @RequestBody SampleCreateRequest request) {
        log.info("Creating sample: {}", request.getName());
        
        // 중복 검사 (예시)
        if ("duplicate".equals(request.getName())) {
            return AxResponseEntity.conflict(ErrorCode.SAMPLE_ALREADY_EXISTS);
        }
        
        SampleResponse sample = SampleResponse.builder()
                .id(ID_GENERATOR.getAndIncrement())
                .name(request.getName())
                .description(request.getDescription())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
        
        return AxResponseEntity.created(sample, "샘플이 성공적으로 생성되었습니다.");
    }

    /**
     * 샘플 수정
     * 
     * @param id 샘플 ID
     * @param request 샘플 수정 요청
     * @return 수정된 샘플 정보
     */
    @Operation(summary = "샘플 수정", description = "기존 샘플 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "샘플 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "샘플을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public AxResponseEntity<SampleResponse> updateSample(
            @PathVariable @Positive Long id, 
            @Valid @RequestBody SampleUpdateRequest request) {
        log.info("Updating sample with id: {}", id);
        
        // 샘플이 없는 경우 에러 응답
        if (id > 100) {
            return AxResponseEntity.notFound(ErrorCode.SAMPLE_NOT_FOUND);
        }
        
        SampleResponse sample = SampleResponse.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();
        
        return AxResponseEntity.updated(sample, "샘플 정보가 성공적으로 수정되었습니다.");
    }

    /**
     * 샘플 삭제
     * 
     * @param id 샘플 ID
     * @return 삭제 성공 응답
     */
    @Operation(summary = "샘플 삭제", description = "기존 샘플을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "샘플 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "샘플을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public AxResponseEntity<Void> deleteSample(@PathVariable @Positive Long id) {
        log.info("Deleting sample with id: {}", id);
        
        // 샘플이 없는 경우 에러 응답
        if (id > 100) {
            return AxResponseEntity.notFound(ErrorCode.SAMPLE_NOT_FOUND);
        }
        
        return AxResponseEntity.deleted("샘플이 성공적으로 삭제되었습니다.");
    }

    // ==================== 에러 테스트 API ====================

    /**
     * 유효성 검증 오류 테스트
     * 
     * @param request 잘못된 요청 (의도적으로 검증 실패)
     * @return 검증 오류 응답
     */
    @Operation(summary = "유효성 검증 오류 테스트", description = "유효성 검증 실패 시 응답을 테스트합니다.")
    @PostMapping("/validation-test")
    public AxResponseEntity<SampleResponse> testValidation(@Valid @RequestBody SampleCreateRequest request) {
        // 이 메서드는 유효성 검증 실패 시 GlobalExceptionHandler에서 처리됩니다.
        return AxResponseEntity.ok(null, "검증 성공");
    }

    /**
     * 비즈니스 로직 오류 테스트
     * 
     * @return 비즈니스 오류 응답
     */
    @Operation(summary = "비즈니스 로직 오류 테스트", description = "비즈니스 규칙 위반 시 응답을 테스트합니다.")
    @PostMapping("/business-error-test")
    public AxResponseEntity<Void> testBusinessError() {
        return AxResponseEntity.conflict(ErrorCode.BUSINESS_RULE_VIOLATION);
    }

    /**
     * 인증 오류 테스트
     * 
     * @return 인증 오류 응답
     */
    @Operation(summary = "인증 오류 테스트", description = "인증 실패 시 응답을 테스트합니다.")
    @PostMapping("/auth-error-test")
    public AxResponseEntity<Void> testAuthError() {
        return AxResponseEntity.unauthorized(ErrorCode.AUTHENTICATION_FAILED);
    }

    /**
     * 권한 오류 테스트
     * 
     * @return 권한 오류 응답
     */
    @Operation(summary = "권한 오류 테스트", description = "권한 부족 시 응답을 테스트합니다.")
    @PostMapping("/permission-error-test")
    public AxResponseEntity<Void> testPermissionError() {
        return AxResponseEntity.forbidden(ErrorCode.ACCESS_DENIED);
    }

    /**
     * 서버 오류 테스트
     * 
     * @return 서버 오류 응답
     */
    @Operation(summary = "서버 오류 테스트", description = "서버 내부 오류 시 응답을 테스트합니다.")
    @PostMapping("/server-error-test")
    public AxResponseEntity<Void> testServerError() {
        return AxResponseEntity.internalServerError(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    // ==================== DTO 클래스들 ====================

    /**
     * 샘플 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleResponse {
        private Long id;
        private String name;
        private String description;
        private String status;
        private LocalDateTime createdAt;
    }

    /**
     * 샘플 생성 요청 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleCreateRequest {
        @NotBlank(message = "샘플명은 필수입니다")
        private String name;

        @NotBlank(message = "설명은 필수입니다")
        private String description;
    }

    /**
     * 샘플 수정 요청 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SampleUpdateRequest {
        @NotBlank(message = "샘플명은 필수입니다")
        private String name;

        @NotBlank(message = "설명은 필수입니다")
        private String description;
    }
}
