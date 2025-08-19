package com.skax.core.common.exception;

import com.skax.core.common.response.ErrorCode;

/**
 * 비즈니스 로직 예외 클래스
 * 
 * <p>비즈니스 규칙 위반이나 업무 로직 관련 예외를 처리하기 위한 클래스입니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
public class BusinessException extends CustomException {

    /**
     * ErrorCode를 사용한 비즈니스 예외 생성
     * 
     * @param errorCode 에러 코드
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * ErrorCode와 상세 정보를 사용한 비즈니스 예외 생성
     * 
     * @param errorCode 에러 코드
     * @param details 에러 상세 정보
     */
    public BusinessException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }

    /**
     * ErrorCode와 원인 예외를 사용한 비즈니스 예외 생성
     * 
     * @param errorCode 에러 코드
     * @param cause 원인 예외
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * ErrorCode, 상세 정보, 원인 예외를 사용한 비즈니스 예외 생성
     * 
     * @param errorCode 에러 코드
     * @param details 에러 상세 정보
     * @param cause 원인 예외
     */
    public BusinessException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode, details, cause);
    }
}
