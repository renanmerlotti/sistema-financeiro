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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

        Page<User> page = new PageImpl<>(List.of(user1, user2));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserResponseDTO> pageDTOs = userService.getAllUsers(Pageable.unpaged());

        assertEquals(2, pageDTOs.getTotalElements());
        assertEquals("vitor", pageDTOs.getContent().get(0).getUsername());
        assertEquals("gabriel", pageDTOs.getContent().get(1).getUsername());
    }
}