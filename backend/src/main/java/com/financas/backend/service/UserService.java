package com.financas.backend.service;

import com.financas.backend.dto.request.UserRequestDTO;
import com.financas.backend.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long userId);
}
