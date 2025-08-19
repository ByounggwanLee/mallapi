package com.skax.core.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의 열거형
 * 
 * <p>애플리케이션에서 발생하는 모든 에러에 대한 표준화된 코드와 메시지를 정의합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ==================== 사용자 관련 (U001~U099) ====================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다"),
    USER_INACTIVE(HttpStatus.FORBIDDEN, "U003", "비활성화된 사용자입니다"),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U004", "이미 존재하는 이메일입니다"),
    USER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U005", "비밀번호가 일치하지 않습니다"),

    // ==================== 회원 관련 (M001~M099) ====================
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "회원을 찾을 수 없습니다"),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "M002", "이미 존재하는 회원입니다"),
    MEMBER_INACTIVE(HttpStatus.FORBIDDEN, "M003", "비활성화된 회원입니다"),
    MEMBER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "M004", "회원 역할을 찾을 수 없습니다"),
    MEMBER_ROLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "M005", "이미 존재하는 회원 역할입니다"),

    // ==================== 역할 관련 (R001~R099) ====================
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "역할을 찾을 수 없습니다"),
    ROLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "R002", "이미 존재하는 역할입니다"),
    ROLE_IN_USE(HttpStatus.CONFLICT, "R003", "사용 중인 역할은 삭제할 수 없습니다"),

    // ==================== 할일 관련 (T001~T099) ====================
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "할일을 찾을 수 없습니다"),
    TODO_ALREADY_EXISTS(HttpStatus.CONFLICT, "T002", "이미 존재하는 할일입니다"),
    TODO_ALREADY_COMPLETED(HttpStatus.CONFLICT, "T003", "이미 완료된 할일입니다"),
    TODO_NOT_COMPLETED(HttpStatus.CONFLICT, "T004", "완료되지 않은 할일입니다"),
    TODO_ACCESS_DENIED(HttpStatus.FORBIDDEN, "T005", "할일에 접근할 권한이 없습니다"),

    // ==================== 상품 관련 (P001~P099) ====================
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품을 찾을 수 없습니다"),
    PRODUCT_ALREADY_EXISTS(HttpStatus.CONFLICT, "P002", "이미 존재하는 상품입니다"),
    PRODUCT_OUT_OF_STOCK(HttpStatus.CONFLICT, "P003", "재고가 부족합니다"),
    PRODUCT_PRICE_INVALID(HttpStatus.BAD_REQUEST, "P004", "잘못된 상품 가격입니다"),
    PRODUCT_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, "P005", "잘못된 상품 카테고리입니다"),

    // ==================== 샘플 관련 (S001~S099) ====================
    SAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "샘플을 찾을 수 없습니다"),
    SAMPLE_ALREADY_EXISTS(HttpStatus.CONFLICT, "S002", "이미 존재하는 샘플입니다"),

    // ==================== 공통 오류 (C001~C099) ====================
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "C002", "입력값 검증에 실패했습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청한 리소스를 찾을 수 없습니다"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "C004", "중복된 리소스입니다"),
    INVALID_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "C005", "잘못된 요청 형식입니다"),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "C006", "필수 파라미터가 누락되었습니다"),

    // ==================== 인증/인가 오류 (A001~A099) ====================
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A001", "인증에 실패했습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A003", "토큰이 만료되었습니다"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A004", "유효하지 않은 토큰입니다"),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "A005", "토큰이 누락되었습니다"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A006", "리프레시 토큰이 만료되었습니다"),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A007", "유효하지 않은 리프레시 토큰입니다"),
    INSUFFICIENT_PRIVILEGES(HttpStatus.FORBIDDEN, "A008", "권한이 부족합니다"),

    // ==================== 서버 오류 (E001~E099) ====================
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "서버 내부 오류가 발생했습니다"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.BAD_GATEWAY, "E002", "외부 서비스 오류가 발생했습니다"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E003", "데이터베이스 오류가 발생했습니다"),
    NETWORK_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "E004", "네트워크 오류가 발생했습니다"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "E005", "서비스를 사용할 수 없습니다"),
    TIMEOUT_ERROR(HttpStatus.REQUEST_TIMEOUT, "E006", "요청 시간이 초과되었습니다"),

    // ==================== 비즈니스 로직 오류 (B001~B099) ====================
    BUSINESS_RULE_VIOLATION(HttpStatus.CONFLICT, "B001", "비즈니스 규칙을 위반했습니다"),
    OPERATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "B002", "허용되지 않은 작업입니다"),
    INVALID_STATE_TRANSITION(HttpStatus.CONFLICT, "B003", "잘못된 상태 변경입니다"),
    QUOTA_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "B004", "할당량을 초과했습니다"),
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "B005", "요청 횟수 제한을 초과했습니다");

    /**
     * HTTP 상태 코드
     */
    private final HttpStatus status;

    /**
     * 구체적인 에러 코드
     */
    private final String code;

    /**
     * 에러 메시지
     */
    private final String message;

    /**
     * HTTP 상태 코드를 문자열로 반환합니다.
     * 
     * @return HTTP 상태 코드 문자열
     */
    public String getHttpStatusCode() {
        return this.status.name();
    }
}
