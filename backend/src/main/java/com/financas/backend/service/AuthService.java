package com.financas.backend.service;

import com.financas.backend.dto.request.LoginRequestDTO;
import com.financas.backend.dto.request.UserRequestDTO;
import com.financas.backend.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(UserRequestDTO dto);
    AuthResponseDTO login(LoginRequestDTO dto);
}
