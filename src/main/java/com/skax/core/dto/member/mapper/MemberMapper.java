package com.skax.core.dto.member.mapper;

import com.skax.core.dto.member.request.MemberCreateRequest;
import com.skax.core.dto.member.request.MemberUpdateRequest;
import com.skax.core.dto.member.response.MemberResponse;
import com.skax.core.entity.member.Member;
import com.skax.core.entity.member.MemberRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Member 엔티티와 DTO 간의 매핑을 담당하는 MapStruct 매퍼
 * 
 * @author ByounggwanLee
 * @since 2025-08-23
 * @version 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MemberMapper {

    /**
     * MemberCreateRequest를 Member 엔티티로 변환합니다.
     * 
     * @param request Member 생성 요청 DTO
     * @return Member 엔티티
     */
    @Mapping(target = "memberRoleList", ignore = true)
    @Mapping(target = "social", constant = "false")
    @Mapping(target = "pw", source = "password")
    Member toEntity(MemberCreateRequest request);

    /**
     * Member 엔티티를 MemberResponse DTO로 변환합니다.
     * 
     * @param member Member 엔티티
     * @return MemberResponse DTO
     */
    @Mapping(target = "roles", source = "memberRoleList", qualifiedByName = "mapRoles")
    @Mapping(target = "name", source = "nickname")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pictureUrl", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    MemberResponse toResponse(Member member);

    /**
     * MemberUpdateRequest DTO를 Member 엔티티에 적용합니다.
     * 
     * @param request MemberUpdateRequest DTO
     * @param member 수정할 Member 엔티티
     */
    @Mapping(target = "memberRoleList", ignore = true)
    @Mapping(target = "pw", ignore = true)
    @Mapping(target = "social", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "nickname", source = "name")
    void updateFromRequest(MemberUpdateRequest request, @MappingTarget Member member);

    /**
     * MemberRole List를 역할 이름 Set으로 변환합니다.
     * 
     * @param memberRoles MemberRole List
     * @return 역할 이름 Set
     */
    @Named("mapRoles")
    default Set<String> mapRoles(List<MemberRole> memberRoles) {
        if (memberRoles == null) {
            return Set.of();
        }
        return memberRoles.stream()
                .map(memberRole -> memberRole.name())
                .collect(Collectors.toSet());
    }
}
