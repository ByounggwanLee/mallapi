package com.skax.core.common.exception;

import com.skax.core.common.response.AxResponse;
import com.skax.core.common.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 전역 예외 처리 핸들러
 * 
 * <p>애플리케이션에서 발생하는 모든 예외를 처리하여 표준화된 응답을 제공합니다.
 * AxResponse 구조를 사용하여 일관된 에러 응답을 보장합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 커스텀 예외 처리 ====================

    /**
     * CustomException 처리
     * 
     * @param e 커스텀 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<AxResponse<Void>> handleCustomException(CustomException e, HttpServletRequest request) {
        log.error("CustomException occurred: {}", e.getMessage(), e);
        
        ErrorCode errorCode = e.getErrorCode();
        
        AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
                .hscode(errorCode.getHttpStatusCode())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .details(e.getDetails() != null ? e.getDetails() : errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        AxResponse<Void> response = AxResponse.<Void>builder()
                .success(false)
                .message(errorCode.getMessage())
                .error(errorInfo)
                .statusCode(errorCode.getStatus().value())
                .statusText(errorCode.getStatus().getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * BusinessException 처리
     * 
     * @param e 비즈니스 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<AxResponse<Void>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("BusinessException occurred: {}", e.getMessage());
        return handleCustomException(e, request);
    }

    // ==================== Spring 유효성 검증 예외 처리 ====================

    /**
     * MethodArgumentNotValidException 처리 (Request Body 검증 실패)
     * 
     * @param e 메서드 인자 검증 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AxResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("Validation failed: {}", e.getMessage());
        
        List<AxResponse.FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> AxResponse.FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
        
        AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
                .hscode("BAD_REQUEST")
                .code("V001")
                .message("입력값이 올바르지 않습니다")
                .details("필수 필드가 누락되었거나 형식이 올바르지 않습니다")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();
        
        AxResponse<Void> response = AxResponse.<Void>builder()
                .success(false)
                .message("입력값 검증에 실패했습니다")
                .error(errorInfo)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .statusText(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * BindException 처리 (쿼리 파라미터 검증 실패)
     * 
     * @param e 바인딩 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<AxResponse<Void>> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("Bind validation failed: {}", e.getMessage());
        
        List<AxResponse.FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> AxResponse.FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
        
        return createValidationErrorResponse("파라미터 검증에 실패했습니다", fieldErrors, request);
    }

    /**
     * Exception 처리 (모든 예외의 최종 처리)
     * 
     * @param e 일반 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AxResponse<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, request);
    }

    // ==================== 헬퍼 메서드 ====================

    /**
     * ErrorCode 기반 에러 응답 생성
     * 
     * @param errorCode 에러 코드
     * @param request HTTP 요청
     * @return 에러 응답
     */
    private ResponseEntity<AxResponse<Void>> createErrorResponse(ErrorCode errorCode, HttpServletRequest request) {
        AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
                .hscode(errorCode.getHttpStatusCode())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        AxResponse<Void> response = AxResponse.<Void>builder()
                .success(false)
                .message(errorCode.getMessage())
                .error(errorInfo)
                .statusCode(errorCode.getStatus().value())
                .statusText(errorCode.getStatus().getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * 유효성 검증 에러 응답 생성
     * 
     * @param message 에러 메시지
     * @param fieldErrors 필드 에러 목록
     * @param request HTTP 요청
     * @return 에러 응답
     */
    private ResponseEntity<AxResponse<Void>> createValidationErrorResponse(
            String message, List<AxResponse.FieldError> fieldErrors, HttpServletRequest request) {
        
        AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
                .hscode("BAD_REQUEST")
                .code("V001")
                .message(message)
                .details("입력값이 올바르지 않습니다")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();
        
        AxResponse<Void> response = AxResponse.<Void>builder()
                .success(false)
                .message(message)
                .error(errorInfo)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .statusText(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }
}
