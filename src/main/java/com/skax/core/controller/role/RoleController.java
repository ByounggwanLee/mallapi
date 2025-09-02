package com.skax.core.controller.role;

import com.skax.core.common.response.AxResponseEntity;
import com.skax.core.common.response.PageResponse;
import com.skax.core.dto.role.request.RoleCreateRequest;
import com.skax.core.dto.role.request.RoleUpdateRequest;
import com.skax.core.dto.role.response.RoleResponse;
import com.skax.core.service.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 역할 관리 컨트롤러
 * 
 * <p>역할(권한)의 생성, 조회, 수정, 삭제 등의 REST API를 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "역할 관리 API")
public class RoleController {

    private final RoleService roleService;

    /**
     * 새로운 역할을 등록합니다.
     * 
     * @param request 역할 생성 요청 데이터
     * @return 생성된 역할 정보
     */
    @Operation(summary = "역할 등록", description = "새로운 역할을 등록합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "역할 등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "역할명 중복")
    })
    @PostMapping
    public AxResponseEntity<RoleResponse> createRole(
            @Valid @RequestBody RoleCreateRequest request) {
        log.info("역할 등록 요청 - 역할명: {}", request.getRoleName());
        
        RoleResponse role = roleService.createRole(request);
        return AxResponseEntity.created(role, "역할이 성공적으로 등록되었습니다.");
    }

    /**
     * 역할 정보를 조회합니다.
     * 
     * @param roleId 역할 ID
     * @return 역할 정보
     */
    @Operation(summary = "역할 정보 조회", description = "역할 ID로 역할 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "역할 조회 성공"),
        @ApiResponse(responseCode = "404", description = "역할을 찾을 수 없음")
    })
    @GetMapping("/{roleId}")
    public AxResponseEntity<RoleResponse> getRole(
            @Parameter(description = "역할 ID", example = "1")
            @PathVariable Long roleId) {
        log.info("역할 정보 조회 - 역할 ID: {}", roleId);
        
        RoleResponse role = roleService.getRoleById(roleId);
        return AxResponseEntity.ok(role, "역할 정보를 성공적으로 조회했습니다.");
    }

    /**
     * 역할명으로 역할을 조회합니다.
     * 
     * @param roleName 역할명
     * @return 역할 정보
     */
    @Operation(summary = "역할명으로 조회", description = "역할명으로 역할 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "역할 조회 성공"),
        @ApiResponse(responseCode = "404", description = "역할을 찾을 수 없음")
    })
    @GetMapping("/name/{roleName}")
    public AxResponseEntity<RoleResponse> getRoleByName(
            @Parameter(description = "역할명", example = "ADMIN")
            @PathVariable String roleName) {
        log.info("역할명으로 조회 - 역할명: {}", roleName);
        
        RoleResponse role = roleService.getRoleByName(roleName);
        return AxResponseEntity.ok(role, "역할 정보를 성공적으로 조회했습니다.");
    }

    /**
     * 역할 정보를 수정합니다.
     * 
     * @param roleId 역할 ID
     * @param request 역할 수정 요청 데이터
     * @return 수정된 역할 정보
     */
    @Operation(summary = "역할 정보 수정", description = "역할 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "역할 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "역할을 찾을 수 없음")
    })
    @PutMapping("/{roleId}")
    public AxResponseEntity<RoleResponse> updateRole(
            @Parameter(description = "역할 ID", example = "1")
            @PathVariable Long roleId,
            @Valid @RequestBody RoleUpdateRequest request) {
        log.info("역할 정보 수정 - 역할 ID: {}", roleId);
        
        RoleResponse role = roleService.updateRole(roleId, request);
        return AxResponseEntity.updated(role, "역할 정보가 성공적으로 수정되었습니다.");
    }

    /**
     * 역할을 삭제합니다.
     * 
     * @param roleId 역할 ID
     */
    @Operation(summary = "역할 삭제", description = "역할을 삭제합니다 (논리적 삭제).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "역할 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "기본 역할은 삭제할 수 없음"),
        @ApiResponse(responseCode = "404", description = "역할을 찾을 수 없음")
    })
    @DeleteMapping("/{roleId}")
    public AxResponseEntity<Void> deleteRole(
            @Parameter(description = "역할 ID", example = "1")
            @PathVariable Long roleId) {
        log.info("역할 삭제 - 역할 ID: {}", roleId);
        
        roleService.deleteRole(roleId);
        return AxResponseEntity.deleted("역할이 성공적으로 삭제되었습니다.");
    }

    /**
     * 모든 역할을 페이징하여 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 역할 목록
     */
    @Operation(summary = "역할 목록 조회", description = "모든 역할을 페이징하여 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "역할 목록 조회 성공")
    })
    @GetMapping
    public AxResponseEntity<PageResponse<RoleResponse>> getAllRoles(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("역할 목록 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        PageResponse<RoleResponse> roles = roleService.getAllRoles(pageable);
        return AxResponseEntity.okPage(roles, "역할 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 모든 활성 역할을 목록으로 조회합니다.
     * 
     * @return 활성 역할 목록
     */
    @Operation(summary = "활성 역할 목록 조회", description = "모든 활성 역할을 목록으로 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "활성 역할 목록 조회 성공")
    })
    @GetMapping("/active")
    public AxResponseEntity<List<RoleResponse>> getAllActiveRoles() {
        log.info("활성 역할 목록 조회");
        
        List<RoleResponse> roles = roleService.getAllActiveRoles();
        return AxResponseEntity.ok(roles, "활성 역할 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 역할명으로 역할을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 역할 목록
     */
    @Operation(summary = "역할 검색", description = "역할명으로 역할을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "역할 검색 성공")
    })
    @GetMapping("/search")
    public AxResponseEntity<PageResponse<RoleResponse>> searchRoles(
            @Parameter(description = "검색 키워드", example = "ADMIN")
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("역할 검색 - 키워드: {}", keyword);
        
        PageResponse<RoleResponse> roles = roleService.searchRolesByName(keyword, pageable);
        return AxResponseEntity.okPage(roles, "역할 검색을 성공적으로 완료했습니다.");
    }

    /**
     * 기본 역할을 설정합니다.
     * 
     * @param roleId 기본 역할로 설정할 역할 ID
     */
    @Operation(summary = "기본 역할 설정", description = "특정 역할을 기본 역할로 설정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "기본 역할 설정 성공"),
        @ApiResponse(responseCode = "404", description = "역할을 찾을 수 없음")
    })
    @PutMapping("/{roleId}/default")
    public AxResponseEntity<Void> setDefaultRole(
            @Parameter(description = "역할 ID", example = "1")
            @PathVariable Long roleId) {
        log.info("기본 역할 설정 - 역할 ID: {}", roleId);
        
        roleService.setDefaultRole(roleId);
        return AxResponseEntity.updated(null, "기본 역할이 성공적으로 설정되었습니다.");
    }

    /**
     * 기본 역할을 조회합니다.
     * 
     * @return 기본 역할 정보
     */
    @Operation(summary = "기본 역할 조회", description = "현재 설정된 기본 역할을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "기본 역할 조회 성공"),
        @ApiResponse(responseCode = "404", description = "설정된 기본 역할 없음")
    })
    @GetMapping("/default")
    public AxResponseEntity<RoleResponse> getDefaultRole() {
        log.info("기본 역할 조회");
        
        RoleResponse role = roleService.getDefaultRole();
        if (role != null) {
            return AxResponseEntity.ok(role, "기본 역할을 성공적으로 조회했습니다.");
        } else {
            return AxResponseEntity.ok(null, "설정된 기본 역할이 없습니다.");
        }
    }
}
