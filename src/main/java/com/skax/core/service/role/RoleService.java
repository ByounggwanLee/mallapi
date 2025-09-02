package com.skax.core.service.role;

import com.skax.core.dto.role.request.RoleCreateRequest;
import com.skax.core.dto.role.request.RoleUpdateRequest;
import com.skax.core.dto.role.response.RoleResponse;
import com.skax.core.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 역할 관리 서비스 인터페이스
 * 
 * <p>역할(권한)의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 정의합니다.</p>
 * 
 * <p>주요 기능:</p>
 * <ul>
 *   <li>역할 등록, 수정, 삭제 (논리적 삭제)</li>
 *   <li>역할 목록 조회 (페이징)</li>
 *   <li>역할명 기반 검색</li>
 *   <li>역할별 회원 수 조회</li>
 *   <li>기본 역할 설정</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
public interface RoleService {

    /**
     * 새로운 역할을 등록합니다.
     * 
     * @param request 역할 생성 요청 정보
     * @return 생성된 역할 정보
     * @throws IllegalArgumentException 이미 존재하는 역할명인 경우
     */
    RoleResponse createRole(RoleCreateRequest request);

    /**
     * 역할 정보를 수정합니다.
     * 
     * @param roleId 역할 ID
     * @param request 역할 수정 요청 정보
     * @return 수정된 역할 정보
     * @throws IllegalArgumentException 존재하지 않는 역할이거나 삭제된 역할인 경우
     */
    RoleResponse updateRole(Long roleId, RoleUpdateRequest request);

    /**
     * 역할을 삭제합니다 (논리적 삭제).
     * 
     * @param roleId 삭제할 역할 ID
     * @throws IllegalArgumentException 존재하지 않는 역할이거나 기본 역할인 경우
     */
    void deleteRole(Long roleId);

    /**
     * 삭제된 역할을 복구합니다.
     * 
     * @param roleId 복구할 역할 ID
     * @throws IllegalArgumentException 존재하지 않는 역할인 경우
     */
    void restoreRole(Long roleId);

    /**
     * 역할 ID로 역할을 조회합니다.
     * 
     * @param roleId 조회할 역할 ID
     * @return 역할 정보
     * @throws IllegalArgumentException 존재하지 않는 역할이거나 삭제된 역할인 경우
     */
    RoleResponse getRoleById(Long roleId);

    /**
     * 역할명으로 역할을 조회합니다.
     * 
     * @param roleName 조회할 역할명
     * @return 역할 정보
     * @throws IllegalArgumentException 존재하지 않는 역할이거나 삭제된 역할인 경우
     */
    RoleResponse getRoleByName(String roleName);

    /**
     * 모든 활성 역할을 페이징하여 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 역할 목록
     */
    PageResponse<RoleResponse> getAllRoles(Pageable pageable);

    /**
     * 모든 활성 역할을 목록으로 조회합니다.
     * 
     * @return 활성 역할 목록
     */
    List<RoleResponse> getAllActiveRoles();

    /**
     * 역할명으로 역할을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 역할 목록
     */
    PageResponse<RoleResponse> searchRolesByName(String keyword, Pageable pageable);

    /**
     * 특정 역할이 존재하는지 확인합니다.
     * 
     * @param roleName 확인할 역할명
     * @return 역할이 존재하면 true, 아니면 false
     */
    boolean existsByRoleName(String roleName);

    /**
     * 기본 역할인지 확인합니다.
     * 
     * @param roleId 확인할 역할 ID
     * @return 기본 역할이면 true, 아니면 false
     * @throws IllegalArgumentException 존재하지 않는 역할인 경우
     */
    boolean isDefaultRole(Long roleId);

    /**
     * 기본 역할을 설정합니다.
     * 
     * @param roleId 기본 역할로 설정할 역할 ID
     * @throws IllegalArgumentException 존재하지 않는 역할이거나 삭제된 역할인 경우
     */
    void setDefaultRole(Long roleId);

    /**
     * 기본 역할 설정을 해제합니다.
     * 
     * @param roleId 기본 역할 설정을 해제할 역할 ID
     * @throws IllegalArgumentException 존재하지 않는 역할인 경우
     */
    void unsetDefaultRole(Long roleId);

    /**
     * 기본 역할을 조회합니다.
     * 
     * @return 기본 역할 정보 (없으면 null)
     */
    RoleResponse getDefaultRole();

    /**
     * 특정 역할을 가진 회원 수를 조회합니다.
     * 
     * @param roleId 역할 ID
     * @return 해당 역할을 가진 회원 수
     * @throws IllegalArgumentException 존재하지 않는 역할인 경우
     */
    long getMemberCountByRole(Long roleId);

    /**
     * 전체 활성 역할 수를 조회합니다.
     * 
     * @return 전체 활성 역할 수
     */
    long getTotalActiveRoleCount();
}
