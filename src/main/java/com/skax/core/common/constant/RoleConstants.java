package com.skax.core.common.constant;

/**
 * 역할 관련 상수를 정의하는 클래스
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
public final class RoleConstants {

    /**
     * 기본 사용자 역할
     */
    public static final String ROLE_USER = "ROLE_USER";

    /**
     * 관리자 역할
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 매니저 역할
     */
    public static final String ROLE_MANAGER = "ROLE_MANAGER";

    /**
     * 판매자 역할
     */
    public static final String ROLE_SELLER = "ROLE_SELLER";

    private RoleConstants() {
        // 유틸리티 클래스이므로 인스턴스 생성 방지
    }
}
