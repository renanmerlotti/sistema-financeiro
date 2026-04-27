package com.financas.backend.service.impl;

import com.financas.backend.dto.request.UserRequestDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    @DisplayName("User created successfully")
    void createUserCase1() {
        UserRequestDTO dto = new UserRequestDTO("joao", "joao@email.com", "123456");
        User savedUser = new User(1L, "joao", "joao@email.com", "123456");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("joao", result.getUsername());
    }

}