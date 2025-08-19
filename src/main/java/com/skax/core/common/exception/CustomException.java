package com.skax.core.common.exception;

import com.skax.core.common.response.ErrorCode;
import lombok.Getter;

/**
 * 커스텀 예외 기본 클래스
 * 
 * <p>애플리케이션에서 발생하는 모든 커스텀 예외의 기본 클래스입니다.
 * ErrorCode 기반의 표준화된 예외 처리를 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Getter
public class CustomException extends RuntimeException {

    /**
     * 에러 코드
     */
    private final ErrorCode errorCode;

    /**
     * 에러 상세 정보
     */
    private final String details;

    /**
     * ErrorCode를 사용한 예외 생성
     * 
     * @param errorCode 에러 코드
     */
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * ErrorCode와 상세 정보를 사용한 예외 생성
     * 
     * @param errorCode 에러 코드
     * @param details 에러 상세 정보
     */
    public CustomException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * ErrorCode와 원인 예외를 사용한 예외 생성
     * 
     * @param errorCode 에러 코드
     * @param cause 원인 예외
     */
    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * ErrorCode, 상세 정보, 원인 예외를 사용한 예외 생성
     * 
     * @param errorCode 에러 코드
     * @param details 에러 상세 정보
     * @param cause 원인 예외
     */
    public CustomException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = details;
    }
}
