package com.skax.core.service.member.impl;

import com.skax.core.common.response.PageResponse;
import com.skax.core.dto.member.request.MemberCreateRequest;
import com.skax.core.dto.member.request.MemberUpdateRequest;
import com.skax.core.dto.member.response.MemberResponse;
import com.skax.core.entity.member.Member;
import com.skax.core.entity.member.MemberRole;
import com.skax.core.dto.member.mapper.MemberMapper;
import com.skax.core.repository.member.MemberRepository;
import com.skax.core.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 관리 서비스 구현체
 * 
 * <p>회원 정보의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 처리합니다.</p>
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        log.info("Creating new member with email: {}", request.getEmail());
        
        // 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            log.warn("Attempt to create member with existing email: {}", request.getEmail());
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + request.getEmail());
        }
        
        // 닉네임 중복 확인
        if (memberRepository.existsByNickname(request.getNickname())) {
            log.warn("Attempt to create member with existing nickname: {}", request.getNickname());
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다: " + request.getNickname());
        }
        
        // Entity 변환 및 비밀번호 암호화
        Member member = memberMapper.toEntity(request);
        member.changePw(passwordEncoder.encode(request.getPassword()));
        
        // 회원 저장
        Member savedMember = memberRepository.save(member);
        log.info("Successfully created member with id: {}", savedMember.getEmail());
        
        return memberMapper.toResponse(savedMember);
    }

    @Override
    public MemberResponse getMemberByEmail(String email) {
        log.debug("Retrieving member by email: {}", email);
        
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));
        
        return memberMapper.toResponse(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(String email, MemberUpdateRequest request) {
        log.info("Updating member with email: {}", email);
        
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));
        
        // 엔티티 업데이트
        memberMapper.updateFromRequest(request, member);
        
        Member updatedMember = memberRepository.save(member);
        log.info("Successfully updated member with email: {}", email);
        
        return memberMapper.toResponse(updatedMember);
    }

    @Override
    @Transactional
    public void deleteMember(String email) {
        log.info("Deleting member with email: {}", email);
        
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));
        
        memberRepository.delete(member);
        
        log.info("Successfully deleted member with email: {}", email);
    }

    @Override
    public PageResponse<MemberResponse> getAllMembers(Pageable pageable) {
        log.debug("Retrieving all members with pagination: page={}, size={}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Member> memberPage = memberRepository.findAll(pageable);
        Page<MemberResponse> responsePage = memberPage.map(memberMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<MemberResponse> searchMembersByNickname(String nickname, Pageable pageable) {
        log.debug("Searching members by nickname: {} with pagination: page={}, size={}", 
                nickname, pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Member> memberPage = memberRepository.findByNicknameContainingIgnoreCase(nickname, pageable);
        Page<MemberResponse> responsePage = memberPage.map(memberMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<MemberResponse> getMembersBySocial(boolean social, Pageable pageable) {
        log.debug("Retrieving members by social status: {} with pagination: page={}, size={}", 
                social, pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Member> memberPage = memberRepository.findBySocial(social, pageable);
        Page<MemberResponse> responsePage = memberPage.map(memberMapper::toResponse);
        
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        log.info("Changing password for member with email: {}", email);
        
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));
        
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, member.getPw())) {
            log.warn("Invalid current password for member: {}", email);
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다");
        }
        
        // 새 비밀번호 암호화 및 저장
        member.changePw(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        
        log.info("Successfully changed password for member: {}", email);
    }

    @Override
    @Transactional
    public void addRoleToMember(String email, String roleName) {
        log.info("Adding role {} to member: {}", roleName, email);
        
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));
        
        // MemberRole enum으로 변환
        MemberRole memberRole;
        try {
            memberRole = MemberRole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role name: {}", roleName);
            throw new IllegalArgumentException("존재하지 않는 역할입니다: " + roleName);
        }
        
        // 이미 해당 역할을 가지고 있는지 확인
        if (member.getMemberRoleList().contains(memberRole)) {
            log.warn("Member {} already has role: {}", email, roleName);
            throw new IllegalArgumentException("이미 해당 역할을 가지고 있습니다: " + roleName);
        }
        
        // 역할 추가
        member.addRole(memberRole);
        memberRepository.save(member);
        
        log.info("Successfully added role {} to member: {}", roleName, email);
    }

    @Override
    @Transactional
    public void removeRoleFromMember(String email, String roleName) {
        log.info("Removing role {} from member: {}", roleName, email);
        
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));
        
        // MemberRole enum으로 변환
        MemberRole memberRole;
        try {
            memberRole = MemberRole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role name: {}", roleName);
            throw new IllegalArgumentException("존재하지 않는 역할입니다: " + roleName);
        }
        
        // 역할 제거
        member.getMemberRoleList().remove(memberRole);
        memberRepository.save(member);
        
        log.info("Successfully removed role {} from member: {}", roleName, email);
    }

    @Override
    @Transactional
    public void clearMemberRoles(String email) {
        log.info("Clearing all roles for member: {}", email);
        
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + email));
        
        member.clearRole();
        memberRepository.save(member);
        
        log.info("Successfully cleared all roles for member: {}", email);
    }

    @Override
    public boolean isEmailExists(String email) {
        log.debug("Checking if email exists: {}", email);
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean isNicknameExists(String nickname) {
        log.debug("Checking if nickname exists: {}", nickname);
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public long getTotalMemberCount() {
        log.debug("Getting total member count");
        return memberRepository.count();
    }

    @Override
    public long getSocialMemberCount() {
        log.debug("Getting social member count");
        return memberRepository.countBySocial(true);
    }
}
