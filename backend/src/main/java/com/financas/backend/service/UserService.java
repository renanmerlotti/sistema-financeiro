package com.financas.backend.service;

import com.financas.backend.dto.request.UserRegistrationDTO;
import com.financas.backend.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRegistrationDTO userRegistrationDTO);

    UserResponseDTO getUserById(Long userId);
}
