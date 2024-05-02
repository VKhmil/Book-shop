package com.bookappstore.controller;

import com.bookappstore.dto.user.UserLoginRequestDto;
import com.bookappstore.dto.user.UserLoginResponseDto;
import com.bookappstore.dto.user.UserRegistrationRequestDto;
import com.bookappstore.dto.user.UserResponseDto;
import com.bookappstore.exception.RegistrationException;
import com.bookappstore.security.AuthenticationService;
import com.bookappstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Registration management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Endpoint for user registration",
            description = "User registration")
    @PostMapping("/registration")
    public UserResponseDto registerUser(
            @RequestBody UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto loginUser(@RequestBody UserLoginRequestDto userLoginResponseDto) {
        return authenticationService.authenticate(userLoginResponseDto);
    }
}
