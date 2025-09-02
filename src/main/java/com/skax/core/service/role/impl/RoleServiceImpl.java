package com.skax.core.service.role.impl;

import com.skax.core.dto.role.request.RoleCreateRequest;
import com.skax.core.dto.role.request.RoleUpdateRequest;
import com.skax.core.dto.role.response.RoleResponse;
import com.skax.core.common.response.PageResponse;
import com.skax.core.entity.member.Role;
import com.skax.core.repository.member.RoleRepository;
import com.skax.core.service.role.RoleService;
import com.skax.core.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 역할 관리 서비스 구현체
 * 
 * <p>역할(권한)의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 처리합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ServiceUtils serviceUtils;

    @Override
    @Transactional
    public RoleResponse createRole(RoleCreateRequest request) {
        log.info("Creating new role with name: {}", request.getRoleName());
        
        // 역할명 중복 확인
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new IllegalArgumentException("이미 존재하는 역할명입니다: " + request.getRoleName());
        }
        
        // 역할 생성
        Role role = Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .isActive(true)
                .build();
        
        Role savedRole = roleRepository.save(role);
        log.info("Successfully created role with id: {}", savedRole.getId());
        
        RoleResponse response = convertToResponse(savedRole);
        return serviceUtils.mapWithAudit(savedRole, response);
    }

    @Override
    @Transactional
    public RoleResponse updateRole(Long roleId, RoleUpdateRequest request) {
        log.info("Updating role with id: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        if (!role.getIsActive()) {
            throw new IllegalArgumentException("삭제된 역할은 수정할 수 없습니다: " + roleId);
        }
        
        // 역할명 변경 시 중복 확인
        if (request.getRoleName() != null && !request.getRoleName().equals(role.getRoleName())) {
            if (roleRepository.existsByRoleName(request.getRoleName())) {
                throw new IllegalArgumentException("이미 존재하는 역할명입니다: " + request.getRoleName());
            }
            role.setRoleName(request.getRoleName());
        }
        
        // 설명 업데이트
        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }
        
        Role updatedRole = roleRepository.save(role);
        log.info("Successfully updated role with id: {}", roleId);
        
        RoleResponse response = convertToResponse(updatedRole);
        return serviceUtils.mapWithAudit(updatedRole, response);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        log.info("Deleting role with id: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        // 기본 역할 삭제 방지 (예: ROLE_USER)
        if ("ROLE_USER".equals(role.getRoleName())) {
            throw new IllegalArgumentException("기본 역할은 삭제할 수 없습니다: " + role.getRoleName());
        }
        
        // 논리적 삭제
        role.setIsActive(false);
        roleRepository.save(role);
        
        log.info("Successfully deleted role with id: {}", roleId);
    }

    @Override
    @Transactional
    public void restoreRole(Long roleId) {
        log.info("Restoring role with id: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        role.setIsActive(true);
        roleRepository.save(role);
        
        log.info("Successfully restored role with id: {}", roleId);
    }

    @Override
    public RoleResponse getRoleById(Long roleId) {
        log.debug("Getting role by id: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        if (!role.getIsActive()) {
            throw new IllegalArgumentException("삭제된 역할입니다: " + roleId);
        }
        
        RoleResponse response = convertToResponse(role);
        return serviceUtils.mapWithAudit(role, response);
    }

    @Override
    public RoleResponse getRoleByName(String roleName) {
        log.debug("Getting role by name: {}", roleName);
        
        Role role = roleRepository.findByRoleNameAndIsActiveTrue(roleName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleName));
        
        RoleResponse response = convertToResponse(role);
        return serviceUtils.mapWithAudit(role, response);
    }

    @Override
    public PageResponse<RoleResponse> getAllRoles(Pageable pageable) {
        log.debug("Getting all roles with pagination: {}", pageable);
        
        Page<Role> rolePage = roleRepository.findAll(pageable);
        
        List<RoleResponse> roleResponses = rolePage.getContent().stream()
                .filter(Role::getIsActive)
                .map(role -> {
                    RoleResponse response = convertToResponse(role);
                    return serviceUtils.mapWithAudit(role, response);
                })
                .toList();
        
        return PageResponse.<RoleResponse>builder()
                .content(roleResponses)
                .page(rolePage.getNumber())
                .size(rolePage.getSize())
                .totalElements(rolePage.getTotalElements())
                .totalPages(rolePage.getTotalPages())
                .first(rolePage.isFirst())
                .last(rolePage.isLast())
                .build();
    }

    @Override
    public List<RoleResponse> getAllActiveRoles() {
        log.debug("Getting all active roles");
        
        List<Role> roles = roleRepository.findAll().stream()
                .filter(Role::getIsActive)
                .toList();
        
        return roles.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public PageResponse<RoleResponse> searchRolesByName(String keyword, Pageable pageable) {
        log.debug("Searching roles by keyword: {}", keyword);
        
        Page<Role> rolePage = roleRepository.findAll(pageable);
        
        List<RoleResponse> filteredRoles = rolePage.getContent().stream()
                .filter(Role::getIsActive)
                .filter(role -> role.getRoleName().toLowerCase().contains(keyword.toLowerCase()) ||
                               (role.getDescription() != null && role.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .map(this::convertToResponse)
                .toList();
        
        return PageResponse.<RoleResponse>builder()
                .content(filteredRoles)
                .page(rolePage.getNumber())
                .size(rolePage.getSize())
                .totalElements((long) filteredRoles.size())
                .totalPages(rolePage.getTotalPages())
                .first(rolePage.isFirst())
                .last(rolePage.isLast())
                .build();
    }

    @Override
    public boolean existsByRoleName(String roleName) {
        log.debug("Checking if role exists: {}", roleName);
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    public boolean isDefaultRole(Long roleId) {
        log.debug("Checking if role is default: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        // 기본 역할 판단 로직 (예: ROLE_USER)
        return "ROLE_USER".equals(role.getRoleName());
    }

    @Override
    @Transactional
    public void setDefaultRole(Long roleId) {
        log.info("Setting default role: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        if (!role.getIsActive()) {
            throw new IllegalArgumentException("삭제된 역할은 기본 역할로 설정할 수 없습니다: " + roleId);
        }
        
        // 기본 역할 설정 로직 구현 (현재는 단순히 로그만 남김)
        log.info("Role {} set as default", roleId);
    }

    @Override
    @Transactional
    public void unsetDefaultRole(Long roleId) {
        log.info("Unsetting default role: {}", roleId);
        
        roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        // 기본 역할 해제 로직 구현 (현재는 단순히 로그만 남김)
        log.info("Role {} unset as default", roleId);
    }

    @Override
    public RoleResponse getDefaultRole() {
        log.debug("Getting default role");
        
        // 기본 역할 조회 (예: ROLE_USER)
        Role defaultRole = roleRepository.findByRoleNameAndIsActiveTrue("ROLE_USER").orElse(null);
        
        return defaultRole != null ? convertToResponse(defaultRole) : null;
    }

    @Override
    public long getMemberCountByRole(Long roleId) {
        log.debug("Getting member count for role: {}", roleId);
        
        roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다: " + roleId));
        
        // 실제로는 MemberRepository에서 해당 역할을 가진 회원 수를 조회해야 함
        // 현재는 0을 반환
        return 0L;
    }

    @Override
    public long getTotalActiveRoleCount() {
        log.debug("Getting total active role count");
        
        return roleRepository.findAll().stream()
                .filter(Role::getIsActive)
                .count();
    }

    /**
     * Role 엔티티를 RoleResponse DTO로 변환합니다.
     *
     * @param role Role 엔티티
     * @return RoleResponse DTO
     */
    private RoleResponse convertToResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .isDefault("ROLE_USER".equals(role.getRoleName())) // 기본 역할 판단
                .build();
    }
}
