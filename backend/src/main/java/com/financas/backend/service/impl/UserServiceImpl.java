package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.UserMapper;
import com.financas.backend.dto.request.UserRegistrationDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.exception.ConflictException;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.UserRepository;
import com.financas.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRegistrationDTO userRegistrationDTO) {
        if(userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new ConflictException("Email already in use");
        }

        User user = UserMapper.mapUserRegistrationDTOtoUser(userRegistrationDTO);
        User savedUser = userRepository.save(user);

        return UserMapper.mapUserToUserResponseDTO(savedUser);
    }


    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + userId + " not found"));

        return UserMapper.mapUserToUserResponseDTO(user);
    }
}
