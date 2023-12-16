package sas.business.mapper.auth.user;

import sas.model.dto.auth.user.UserCreateDto;
import sas.model.dto.auth.user.UserRequestDto;
import sas.model.dto.auth.user.UserResponseDto;
import sas.model.entity.auth.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDecorator.class)
public interface IUserMapper {
    @Mappings({
            @Mapping(target = "username", source = "entity.username"),
            @Mapping(target = "firstName", source = "entity.firstName"),
            @Mapping(target = "lastName", source = "entity.lastName"),
            @Mapping(target = "emailAddress", source = "entity.emailAddress"),
            @Mapping(target = "profileName", source = "entity.profileName"),
            @Mapping(target = "rightsOnEntity", ignore = true),
    })
    UserResponseDto toResponseDto(User entity);

    @Mappings({
            @Mapping(target = "username", source = "dto.username"),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "salt", ignore = true),
            @Mapping(target = "firstName", source = "dto.firstName"),
            @Mapping(target = "lastName", source = "dto.lastName"),
            @Mapping(target = "emailAddress", source = "dto.emailAddress"),
            @Mapping(target = "profile", ignore = true),            // the profileName in dto is used for updating,
            @Mapping(target = "profileName", ignore = true),        // it must be set in the mapper decorator
            @Mapping(target = "isFirstLogin", ignore = true),
    })
    User toEntity(UserRequestDto dto);

    @Mappings({
            @Mapping(target = "username", source = "dto.username"),
            @Mapping(target = "password", source = "hashedPassword"),
            @Mapping(target = "salt", source = "salt"),
            @Mapping(target = "firstName", source = "dto.firstName"),
            @Mapping(target = "lastName", source = "dto.lastName"),
            @Mapping(target = "emailAddress", source = "dto.emailAddress"),
            @Mapping(target = "profile", ignore = true),        // set in decorator
            @Mapping(target = "profileName", ignore = true),    // set in decorator
            @Mapping(target = "isFirstLogin", ignore = true),   // set in decorator
    })
    User toEntity(UserCreateDto dto, String hashedPassword, String salt);
}
