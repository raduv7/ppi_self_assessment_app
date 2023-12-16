package sas.business.mapper.auth.profile;

import sas.model.dto.auth.profile.ProfileRequestDto;
import sas.model.dto.auth.profile.ProfileResponseDto;
import sas.model.entity.auth.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IProfileMapper {
    @Mappings({
            @Mapping(target = "name", source = "dto.name"),
            @Mapping(target = "rights", source = "dto.rights"),
    })
    Profile toProfile(ProfileRequestDto dto);

    @Mappings({
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "rights", source = "entity.rights"),
            @Mapping(target = "rightsOnEntity", ignore = true),
    })
    ProfileResponseDto toResponseDto(Profile entity, boolean needsRights, boolean needsDetails);
}
