package com.financas.backend.service;

import com.financas.backend.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO getUserById(Long userId);
}
