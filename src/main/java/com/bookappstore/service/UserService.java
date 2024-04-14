package com.bookappstore.service;

import com.bookappstore.dto.user.UserRegistrationRequestDto;
import com.bookappstore.dto.user.UserResponseDto;
import com.bookappstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException;

}
