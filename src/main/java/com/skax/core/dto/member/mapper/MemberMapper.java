package com.skax.core.dto.member.mapper;

import com.skax.core.dto.member.request.MemberUpdateRequest;
import com.skax.core.dto.member.response.MemberResponse;
import com.skax.core.entity.member.Member;
import com.skax.core.entity.member.MemberRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Member 엔티티와 DTO 간의 매핑을 담당하는 MapStruct 매퍼
 * 
 * @author ByounggwanLee
 * @since 2025-08-19
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface MemberMapper {

    /**
     * Member 엔티티를 MemberResponse DTO로 변환합니다.
     * 
     * @param member Member 엔티티
     * @return MemberResponse DTO
     */
    @Mapping(target = "roles", source = "memberRoles", qualifiedByName = "mapRoles")
    MemberResponse toResponse(Member member);

    /**
     * MemberUpdateRequest DTO를 Member 엔티티에 적용합니다.
     * 
     * @param request MemberUpdateRequest DTO
     * @param member 수정할 Member 엔티티
     */
    void updateFromRequest(MemberUpdateRequest request, @MappingTarget Member member);

    /**
     * MemberRole Set을 역할 이름 Set으로 변환합니다.
     * 
     * @param memberRoles MemberRole Set
     * @return 역할 이름 Set
     */
    @Named("mapRoles")
    default Set<String> mapRoles(Set<MemberRole> memberRoles) {
        if (memberRoles == null) {
            return Set.of();
        }
        return memberRoles.stream()
                .filter(MemberRole::getIsActive)
                .map(memberRole -> memberRole.getRole().getRoleName())
                .collect(Collectors.toSet());
    }
}
