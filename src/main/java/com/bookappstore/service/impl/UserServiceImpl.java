package com.bookappstore.service.impl;

import com.bookappstore.dto.user.UserRegistrationRequestDto;
import com.bookappstore.dto.user.UserResponseDto;
import com.bookappstore.exception.RegistrationException;
import com.bookappstore.mapper.UserMapper;
import com.bookappstore.model.User;
import com.bookappstore.repository.user.UserRepository;
import com.bookappstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toModel(requestDto);

        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}
