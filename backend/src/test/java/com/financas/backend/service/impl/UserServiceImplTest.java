package com.financas.backend.service.impl;

import com.financas.backend.dto.request.UserRegistrationDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        UserRegistrationDTO dto = new UserRegistrationDTO("joao", "joao@email.com", "123456");
        User savedUser = new User(1L, "joao", "joao@email.com", "123456");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("joao", result.getUsername());
    }

    @Test
    @DisplayName("Users listed successfully")
    void getAllUsers() {
        User user1 = new User(1L, "vitor", "vitor@gmail.com", "123456");
        User user2 = new User(2L, "gabriel", "gabriel@gmail.com", "654321");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("vitor", result.get(0).getUsername());
        assertEquals("gabriel", result.get(1).getUsername());
    }
}