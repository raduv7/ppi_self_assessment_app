package sas.business.mapper.auth.user;

import sas.infrastructure.repository.auth.IProfileRepository;
import sas.infrastructure.repository.auth.IUserRepository;
import sas.model.dto.auth.user.UserCreateDto;
import sas.model.dto.auth.user.UserRequestDto;
import sas.model.dto.auth.user.UserResponseDto;
import sas.model.entity.auth.Profile;
import sas.model.entity.auth.User;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Mapper(componentModel = "spring")
public abstract class UserMapperDecorator implements IUserMapper {
    @Autowired @Qualifier("delegate") private IUserMapper delegate;

    @Autowired private IProfileRepository profileRepository;
    @Autowired private IUserRepository userRepository;

    @Override
    public UserResponseDto toResponseDto(User entity) {
        return delegate.toResponseDto(entity);
    }

    @Override
    public User toEntity(UserRequestDto dto) {
        User result = delegate.toEntity(dto);

        Profile profile = profileRepository.findByName(dto.getProfileName()).orElseThrow();
        result.setProfile(profile);
        result.setProfileName(profile.getName());

        return result;
    }

    @Override
    public User toEntity(UserCreateDto dto, String hashedPassword, String salt) {
        User result = delegate.toEntity(dto, hashedPassword, salt);

        Profile profile = profileRepository.findByName(dto.getProfileName()).orElseThrow(
                () -> new NoSuchFieldError("Profile not found"));
        result.setProfile(profile);
        result.setProfileName(profile.getName());

        result.setIsFirstLogin(true);

        return result;
    }
}
