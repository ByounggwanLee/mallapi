package com.skax.core.entity.member;

/**
 * 회원 권한을 정의하는 열거형 클래스
 * 
 * <p>시스템 내에서 회원이 가질 수 있는 권한의 종류를 정의합니다.</p>
 * 
 * <ul>
 *   <li>{@link #USER} - 일반 사용자 권한</li>
 *   <li>{@link #MANAGER} - 관리자 권한</li>
 *   <li>{@link #ADMIN} - 최고 관리자 권한</li>
 * </ul>
 * 
 * <p>권한은 계층적 구조를 가지며, 높은 권한은 낮은 권한의 기능을 포함합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
public enum MemberRole {

    /**
     * 일반 사용자 권한
     * 기본적인 시스템 기능을 사용할 수 있는 권한입니다.
     */
    USER,
    
    /**
     * 관리자 권한
     * 일반 사용자 권한에 추가로 관리 기능을 사용할 수 있는 권한입니다.
     */
    MANAGER,
    
    /**
     * 최고 관리자 권한
     * 시스템의 모든 기능을 사용할 수 있는 최고 권한입니다.
     */
    ADMIN
}
