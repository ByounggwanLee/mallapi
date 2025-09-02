package com.skax.core.controller.member;

import com.skax.core.common.response.AxResponseEntity;
import com.skax.core.common.response.PageResponse;
import com.skax.core.dto.member.request.MemberCreateRequest;
import com.skax.core.dto.member.request.MemberUpdateRequest;
import com.skax.core.dto.member.response.MemberResponse;
import com.skax.core.service.member.MemberService;
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

/**
 * 회원 관리 컨트롤러
 * 
 * <p>회원의 생성, 조회, 수정, 삭제 등의 REST API를 제공합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    /**
     * 새로운 회원을 등록합니다.
     * 
     * @param request 회원 생성 요청 데이터
     * @return 생성된 회원 정보
     */
    @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "회원 등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    @PostMapping
    public AxResponseEntity<MemberResponse> createMember(
            @Valid @RequestBody MemberCreateRequest request) {
        log.info("회원 등록 요청 - 이메일: {}", request.getEmail());
        
        MemberResponse member = memberService.createMember(request);
        return AxResponseEntity.created(member, "회원이 성공적으로 등록되었습니다.");
    }

    /**
     * 회원 정보를 조회합니다.
     * 
     * @param memberId 회원 ID
     * @return 회원 정보
     */
    @Operation(summary = "회원 정보 조회", description = "회원 ID로 회원 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @GetMapping("/{memberId}")
    public AxResponseEntity<MemberResponse> getMember(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId) {
        log.info("회원 정보 조회 - 회원 ID: {}", memberId);
        
        MemberResponse member = memberService.getMemberByEmail(memberId);
        return AxResponseEntity.ok(member, "회원 정보를 성공적으로 조회했습니다.");
    }

    /**
     * 회원 정보를 수정합니다.
     * 
     * @param memberId 회원 ID
     * @param request 회원 수정 요청 데이터
     * @return 수정된 회원 정보
     */
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @PutMapping("/{memberId}")
    public AxResponseEntity<MemberResponse> updateMember(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId,
            @Valid @RequestBody MemberUpdateRequest request) {
        log.info("회원 정보 수정 - 회원 ID: {}", memberId);
        
        MemberResponse member = memberService.updateMember(memberId, request);
        return AxResponseEntity.updated(member, "회원 정보가 성공적으로 수정되었습니다.");
    }

    /**
     * 회원을 삭제합니다.
     * 
     * @param memberId 회원 ID
     */
    @Operation(summary = "회원 삭제", description = "회원을 삭제합니다 (논리적 삭제).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
    })
    @DeleteMapping("/{memberId}")
    public AxResponseEntity<Void> deleteMember(
            @Parameter(description = "회원 ID", example = "user@example.com")
            @PathVariable String memberId) {
        log.info("회원 삭제 - 회원 ID: {}", memberId);
        
        memberService.deleteMember(memberId);
        return AxResponseEntity.deleted("회원이 성공적으로 삭제되었습니다.");
    }

    /**
     * 모든 회원을 페이징하여 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 회원 목록
     */
    @Operation(summary = "회원 목록 조회", description = "모든 회원을 페이징하여 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공")
    })
    @GetMapping
    public AxResponseEntity<PageResponse<MemberResponse>> getAllMembers(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("회원 목록 조회 - 페이지: {}, 크기: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        PageResponse<MemberResponse> members = memberService.getAllMembers(pageable);
        return AxResponseEntity.okPage(members, "회원 목록을 성공적으로 조회했습니다.");
    }

    /**
     * 닉네임으로 회원을 검색합니다.
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 검색된 회원 목록
     */
    @Operation(summary = "회원 검색", description = "닉네임으로 회원을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원 검색 성공")
    })
    @GetMapping("/search")
    public AxResponseEntity<PageResponse<MemberResponse>> searchMembers(
            @Parameter(description = "검색 키워드", example = "홍길동")
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("회원 검색 - 키워드: {}", keyword);
        
        PageResponse<MemberResponse> members = memberService.searchMembersByNickname(keyword, pageable);
        return AxResponseEntity.okPage(members, "회원 검색을 성공적으로 완료했습니다.");
    }
}
