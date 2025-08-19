package com.skax.core.common.constant;

/**
 * API 응답 관련 상수를 정의하는 클래스
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
public final class ApiConstants {

    /**
     * API 기본 경로
     */
    public static final String API_BASE_PATH = "/api";

    /**
     * API 버전 1
     */
    public static final String API_V1 = "/v1";

    /**
     * 성공 응답 코드
     */
    public static final String SUCCESS_CODE = "SUCCESS";

    /**
     * 실패 응답 코드
     */
    public static final String ERROR_CODE = "ERROR";

    /**
     * 인증 실패 응답 코드
     */
    public static final String UNAUTHORIZED_CODE = "UNAUTHORIZED";

    /**
     * 권한 없음 응답 코드
     */
    public static final String FORBIDDEN_CODE = "FORBIDDEN";

    /**
     * 리소스 없음 응답 코드
     */
    public static final String NOT_FOUND_CODE = "NOT_FOUND";

    /**
     * 유효성 검사 실패 응답 코드
     */
    public static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

    /**
     * 기본 페이지 크기
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 최대 페이지 크기
     */
    public static final int MAX_PAGE_SIZE = 100;

    private ApiConstants() {
        // 유틸리티 클래스이므로 인스턴스 생성 방지
    }
}
