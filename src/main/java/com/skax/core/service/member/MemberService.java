package com.skax.core.service.member;

import com.skax.core.dto.member.request.MemberCreateRequest;
import com.skax.core.dto.member.request.MemberUpdateRequest;
import com.skax.core.dto.member.response.MemberResponse;
import com.skax.core.common.response.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * 회원 관리 서비스 인터페이스
 * 
 * <p>회원의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 정의합니다.</p>
 * 
 * <p>주요 기능:</p>
 * <ul>
 *   <li>회원 가입 및 정보 관리</li>
 *   <li>로그인 및 인증 처리</li>
 *   <li>소셜 로그인 연동</li>
 *   <li>회원 권한 관리</li>
 *   <li>회원 검색 및 조회</li>
 * </ul>
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
public interface MemberService {

    /**
     * 새로운 회원을 생성합니다.
     * 
     * @param request 회원 생성 요청 정보
     * @return 생성된 회원 정보
     * @throws IllegalArgumentException 이미 존재하는 이메일인 경우
     */
    MemberResponse createMember(MemberCreateRequest request);

    /**
     * 회원 정보를 수정합니다.
     * 
     * @param email 회원 이메일
     * @param request 회원 수정 요청 정보
     * @return 수정된 회원 정보
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    MemberResponse updateMember(String email, MemberUpdateRequest request);

    /**
     * 회원을 삭제합니다.
     * 
     * @param email 삭제할 회원 이메일
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    void deleteMember(String email);

    /**
     * 이메일로 회원을 조회합니다.
     * 
     * @param email 조회할 회원 이메일
     * @return 회원 정보
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    MemberResponse getMemberByEmail(String email);

    /**
     * 모든 회원을 페이징하여 조회합니다.
     * 
     * @param pageable 페이징 정보
     * @return 페이징된 회원 목록
     */
    PageResponse<MemberResponse> getAllMembers(Pageable pageable);

    /**
     * 닉네임으로 회원을 검색합니다.
     * 
     * @param nickname 검색할 닉네임
     * @param pageable 페이징 정보
     * @return 검색된 회원 목록
     */
    PageResponse<MemberResponse> searchMembersByNickname(String nickname, Pageable pageable);

    /**
     * 소셜 로그인 여부로 회원을 조회합니다.
     * 
     * @param social 소셜 로그인 여부
     * @param pageable 페이징 정보
     * @return 조건에 맞는 회원 목록
     */
    PageResponse<MemberResponse> getMembersBySocial(boolean social, Pageable pageable);

    /**
     * 회원의 비밀번호를 변경합니다.
     * 
     * @param email 회원 이메일
     * @param currentPassword 현재 비밀번호
     * @param newPassword 새 비밀번호
     * @throws IllegalArgumentException 현재 비밀번호가 틀린 경우
     */
    void changePassword(String email, String currentPassword, String newPassword);

    /**
     * 회원에게 역할을 추가합니다.
     * 
     * @param email 회원 이메일
     * @param roleName 추가할 역할명
     * @throws IllegalArgumentException 존재하지 않는 회원이나 역할인 경우
     */
    void addRoleToMember(String email, String roleName);

    /**
     * 회원의 역할을 제거합니다.
     * 
     * @param email 회원 이메일
     * @param roleName 제거할 역할명
     * @throws IllegalArgumentException 존재하지 않는 회원이나 역할인 경우
     */
    void removeRoleFromMember(String email, String roleName);

    /**
     * 회원의 모든 역할을 초기화합니다.
     * 
     * @param email 회원 이메일
     * @throws IllegalArgumentException 존재하지 않는 회원인 경우
     */
    void clearMemberRoles(String email);

    /**
     * 이메일 중복을 확인합니다.
     * 
     * @param email 확인할 이메일
     * @return 중복 여부 (true: 중복, false: 사용 가능)
     */
    boolean isEmailExists(String email);

    /**
     * 닉네임 중복을 확인합니다.
     * 
     * @param nickname 확인할 닉네임
     * @return 중복 여부 (true: 중복, false: 사용 가능)
     */
    boolean isNicknameExists(String nickname);

    /**
     * 전체 회원 수를 조회합니다.
     * 
     * @return 전체 회원 수
     */
    long getTotalMemberCount();

    /**
     * 소셜 로그인 회원 수를 조회합니다.
     * 
     * @return 소셜 로그인 회원 수
     */
    long getSocialMemberCount();
}
