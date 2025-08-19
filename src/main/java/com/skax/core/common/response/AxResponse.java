package com.skax.core.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 표준화된 API 응답 클래스
 * 
 * <p>모든 API 응답의 일관성을 보장하고 클라이언트 개발 효율성을 향상시키기 위한 표준 응답 형식입니다.</p>
 * 
 * @param <T> 응답 데이터 타입
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxResponse<T> {

    /**
     * 요청 성공 여부
     */
    private boolean success;

    /**
     * 응답 메시지
     */
    private String message;

    /**
     * 응답 데이터 (성공 시)
     */
    private T data;

    /**
     * 에러 정보 (실패 시)
     */
    private ErrorInfo error;

    /**
     * 응답 생성 시간 (ISO 8601)
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 요청 경로
     */
    private String path;

    /**
     * HTTP 상태 코드
     */
    private Integer statusCode;

    /**
     * HTTP 상태 텍스트
     */
    private String statusText;

    /**
     * 성공 응답을 생성합니다.
     * 
     * @param data 응답 데이터
     * @param message 성공 메시지
     * @param <T> 데이터 타입
     * @return 성공 응답
     */
    public static <T> AxResponse<T> success(T data, String message) {
        return AxResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 성공 응답을 생성합니다. (데이터 없음)
     * 
     * @param message 성공 메시지
     * @return 성공 응답
     */
    public static AxResponse<Void> success(String message) {
        return AxResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();
    }

    /**
     * 실패 응답을 생성합니다.
     * 
     * @param message 실패 메시지
     * @param errorInfo 에러 정보
     * @return 실패 응답
     */
    public static AxResponse<Void> failure(String message, ErrorInfo errorInfo) {
        return AxResponse.<Void>builder()
                .success(false)
                .message(message)
                .error(errorInfo)
                .build();
    }

    /**
     * 실패 응답을 생성합니다.
     * 
     * @param message 실패 메시지
     * @param hscode HTTP 상태 기반 코드
     * @param code 구체적인 에러 코드
     * @param statusCode HTTP 상태 코드
     * @param statusText HTTP 상태 텍스트
     * @return 실패 응답
     */
    public static AxResponse<Void> failure(String message, String hscode, String code, 
                                          Integer statusCode, String statusText) {
        ErrorInfo errorInfo = ErrorInfo.builder()
                .hscode(hscode)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return AxResponse.<Void>builder()
                .success(false)
                .message(message)
                .error(errorInfo)
                .statusCode(statusCode)
                .statusText(statusText)
                .build();
    }

    /**
     * 에러 정보 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorInfo {
        /**
         * HTTP 상태 기반 코드 (예: "NOT_FOUND")
         */
        private String hscode;

        /**
         * 구체적인 에러 코드 (예: "U001")
         */
        private String code;

        /**
         * 에러 메시지
         */
        private String message;

        /**
         * 에러 상세 정보
         */
        private String details;

        /**
         * 에러 발생 시간
         */
        @Builder.Default
        private LocalDateTime timestamp = LocalDateTime.now();

        /**
         * 요청 경로
         */
        private String path;

        /**
         * 유효성 검증 에러 목록
         */
        private List<FieldError> fieldErrors;
    }

    /**
     * 필드 에러 클래스
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        /**
         * 에러가 발생한 필드명
         */
        private String field;

        /**
         * 거부된 값
         */
        private Object rejectedValue;

        /**
         * 에러 메시지
         */
        private String message;
    }
}
