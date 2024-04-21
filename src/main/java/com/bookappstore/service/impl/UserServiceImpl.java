package com.bookappstore.service.impl;

import com.bookappstore.dto.user.UserRegistrationRequestDto;
import com.bookappstore.dto.user.UserResponseDto;
import com.bookappstore.exception.RegistrationException;
import com.bookappstore.mapper.UserMapper;
import com.bookappstore.model.Role;
import com.bookappstore.model.User;
import com.bookappstore.repository.user.UserRepository;
import com.bookappstore.repository.user.role.RoleRepository;
import com.bookappstore.service.UserService;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private Role roleUser;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new RegistrationException("Can't find role by name"));
        Set<Role> defaultUserRoleSet = new HashSet<>();
        defaultUserRoleSet.add(userRole);
        user.setRoles(defaultUserRoleSet);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @PostConstruct
    public void initializeRoles() {
        roleUser = roleRepository.findByName(Role.RoleName.USER)
                .orElseGet(this::createRole);
    }

    private Role createRole() {
        Role role = new Role();
        role.setName(Role.RoleName.USER);
        return roleRepository.save(role);
    }
}
