package com.skax.core.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * 표준화된 API 응답 엔티티 클래스
 * 
 * <p>ResponseEntity와 AxResponse를 통합한 표준화된 응답 래퍼입니다.
 * 모든 REST API 엔드포인트에서 일관된 응답 형식을 제공합니다.</p>
 * 
 * @param <T> 응답 데이터 타입
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
public class AxResponseEntity<T> extends ResponseEntity<AxResponse<T>> {

    private AxResponseEntity(AxResponse<T> body, HttpStatus status) {
        super(body, status);
    }

    // ==================== 성공 응답 메서드들 ====================

    /**
     * 200 OK 응답을 생성합니다.
     * 
     * @param data 응답 데이터
     * @param message 성공 메시지
     * @param <T> 데이터 타입
     * @return 200 OK 응답
     */
    public static <T> AxResponseEntity<T> ok(T data, String message) {
        AxResponse<T> response = AxResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(HttpStatus.OK.value())
                .statusText(HttpStatus.OK.getReasonPhrase())
                .build();
        
        return new AxResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 200 OK 페이징 응답을 생성합니다.
     * 
     * @param pageResponse 페이징 응답 데이터
     * @param message 성공 메시지
     * @param <T> 데이터 타입
     * @return 200 OK 페이징 응답
     */
    public static <T> AxResponseEntity<PageResponse<T>> okPage(PageResponse<T> pageResponse, String message) {
        AxResponse<PageResponse<T>> response = AxResponse.<PageResponse<T>>builder()
                .success(true)
                .message(message)
                .data(pageResponse)
                .statusCode(HttpStatus.OK.value())
                .statusText(HttpStatus.OK.getReasonPhrase())
                .build();
        
        return new AxResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 201 CREATED 응답을 생성합니다.
     * 
     * @param data 생성된 데이터
     * @param message 생성 성공 메시지
     * @param <T> 데이터 타입
     * @return 201 CREATED 응답
     */
    public static <T> AxResponseEntity<T> created(T data, String message) {
        AxResponse<T> response = AxResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(HttpStatus.CREATED.value())
                .statusText(HttpStatus.CREATED.getReasonPhrase())
                .build();
        
        return new AxResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 200 OK 수정 응답을 생성합니다.
     * 
     * @param data 수정된 데이터
     * @param message 수정 성공 메시지
     * @param <T> 데이터 타입
     * @return 200 OK 응답
     */
    public static <T> AxResponseEntity<T> updated(T data, String message) {
        AxResponse<T> response = AxResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .statusCode(HttpStatus.OK.value())
                .statusText(HttpStatus.OK.getReasonPhrase())
                .build();
        
        return new AxResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 200 OK 삭제 응답을 생성합니다.
     * 
     * @param message 삭제 성공 메시지
     * @return 200 OK 응답
     */
    public static AxResponseEntity<Void> deleted(String message) {
        AxResponse<Void> response = AxResponse.<Void>builder()
                .success(true)
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .statusText(HttpStatus.OK.getReasonPhrase())
                .build();
        
        return new AxResponseEntity<>(response, HttpStatus.OK);
    }

    // ==================== ErrorCode 기반 실패 응답 메서드들 (권장) ====================

    /**
     * ErrorCode를 사용한 실패 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return 실패 응답
     */
    public static <T> AxResponseEntity<T> error(ErrorCode errorCode) {
        return error(errorCode, errorCode.getMessage());
    }

    /**
     * ErrorCode와 커스텀 메시지를 사용한 실패 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param customMessage 커스텀 메시지
     * @param <T> 데이터 타입
     * @return 실패 응답
     */
    public static <T> AxResponseEntity<T> error(ErrorCode errorCode, String customMessage) {
        AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
                .hscode(errorCode.getStatus().name())
                .code(errorCode.getCode())
                .message(customMessage)
                .details(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        AxResponse<T> response = AxResponse.<T>builder()
                .success(false)
                .message(customMessage)
                .error(errorInfo)
                .statusCode(errorCode.getStatus().value())
                .statusText(errorCode.getStatus().getReasonPhrase())
                .build();

        return new AxResponseEntity<>(response, errorCode.getStatus());
    }

    // ==================== 구체적 HTTP 상태별 실패 응답 메서드들 ====================

    /**
     * 404 NOT FOUND 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입 
     * @return 404 응답
     */
    public static <T> AxResponseEntity<T> notFound(ErrorCode errorCode) {
        return error(errorCode);
    }

    /**
     * 404 NOT FOUND 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @param code 구체적인 에러 코드
     * @param <T> 데이터 타입
     * @return 404 응답
     */
    public static <T> AxResponseEntity<T> notFound(String message, String code) {
        return createErrorResponse(message, "NOT_FOUND", code, HttpStatus.NOT_FOUND);
    }

    /**
     * 400 BAD REQUEST 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return 400 응답
     */
    public static <T> AxResponseEntity<T> badRequest(ErrorCode errorCode) {
        return error(errorCode);
    }

    /**
     * 400 BAD REQUEST 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @param code 구체적인 에러 코드
     * @param <T> 데이터 타입
     * @return 400 응답
     */
    public static <T> AxResponseEntity<T> badRequest(String message, String code) {
        return createErrorResponse(message, "BAD_REQUEST", code, HttpStatus.BAD_REQUEST);
    }

    /**
     * 401 UNAUTHORIZED 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return 401 응답
     */
    public static <T> AxResponseEntity<T> unauthorized(ErrorCode errorCode) {
        return error(errorCode);
    }

    /**
     * 403 FORBIDDEN 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return 403 응답
     */
    public static <T> AxResponseEntity<T> forbidden(ErrorCode errorCode) {
        return error(errorCode);
    }

    /**
     * 409 CONFLICT 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return 409 응답
     */
    public static <T> AxResponseEntity<T> conflict(ErrorCode errorCode) {
        return error(errorCode);
    }

    /**
     * 500 INTERNAL SERVER ERROR 응답을 생성합니다.
     * 
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return 500 응답
     */
    public static <T> AxResponseEntity<T> internalServerError(ErrorCode errorCode) {
        return error(errorCode);
    }

    // ==================== 기존 방식 (하위 호환성 유지) ====================

    /**
     * 400 BAD REQUEST 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @return 400 응답
     */
    public static AxResponseEntity<Void> badRequest(String message) {
        return createErrorResponse(message, "BAD_REQUEST", null, HttpStatus.BAD_REQUEST);
    }

    /**
     * 401 UNAUTHORIZED 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @return 401 응답
     */
    public static AxResponseEntity<Void> unauthorized(String message) {
        return createErrorResponse(message, "UNAUTHORIZED", null, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 403 FORBIDDEN 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @return 403 응답
     */
    public static AxResponseEntity<Void> forbidden(String message) {
        return createErrorResponse(message, "FORBIDDEN", null, HttpStatus.FORBIDDEN);
    }

    /**
     * 404 NOT FOUND 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @return 404 응답
     */
    public static AxResponseEntity<Void> notFound(String message) {
        return createErrorResponse(message, "NOT_FOUND", null, HttpStatus.NOT_FOUND);
    }

    /**
     * 409 CONFLICT 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @return 409 응답
     */
    public static AxResponseEntity<Void> conflict(String message) {
        return createErrorResponse(message, "CONFLICT", null, HttpStatus.CONFLICT);
    }

    /**
     * 500 INTERNAL SERVER ERROR 응답을 생성합니다.
     * 
     * @param message 에러 메시지
     * @return 500 응답
     */
    public static AxResponseEntity<Void> internalServerError(String message) {
        return createErrorResponse(message, "INTERNAL_SERVER_ERROR", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ==================== 헬퍼 메서드 ====================

    /**
     * 에러 응답을 생성하는 헬퍼 메서드
     * 
     * @param message 에러 메시지
     * @param hscode HTTP 상태 기반 코드
     * @param code 구체적인 에러 코드
     * @param status HTTP 상태
     * @param <T> 데이터 타입
     * @return 에러 응답
     */
    private static <T> AxResponseEntity<T> createErrorResponse(String message, String hscode, String code, HttpStatus status) {
        AxResponse.ErrorInfo errorInfo = AxResponse.ErrorInfo.builder()
                .hscode(hscode)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        AxResponse<T> response = AxResponse.<T>builder()
                .success(false)
                .message(message)
                .error(errorInfo)
                .statusCode(status.value())
                .statusText(status.getReasonPhrase())
                .build();

        return new AxResponseEntity<>(response, status);
    }
}
