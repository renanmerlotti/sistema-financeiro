package com.financas.backend.service;

import com.financas.backend.dto.request.UserRegistrationDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    UserResponseDTO createUser(UserRegistrationDTO userRegistrationDTO);

    Page<UserResponseDTO> getAllUsers(Pageable pageable);
}
