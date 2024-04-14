package com.bookappstore.mapper;

import com.bookappstore.config.MapperConfig;
import com.bookappstore.dto.user.UserRegistrationRequestDto;
import com.bookappstore.dto.user.UserResponseDto;
import com.bookappstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toUserResponseDto(User user);
}
