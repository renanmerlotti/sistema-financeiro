package com.financas.backend.service.impl;

import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.exception.ResourceNotFoundException;
import com.financas.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should return UserResponseDTO when user is found by id")
    void getUserByIdCase1() {
        User user = new User(1L, "renan", "renan@email.com", "12351234");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponseDTO userResponseDTO = userService.getUserById(user.getId());

        assertNotNull(userResponseDTO);
        assertEquals(1L, userResponseDTO.getId());
        assertEquals("renan", userResponseDTO.getUsername());
        assertEquals("renan@email.com", userResponseDTO.getEmail());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user id does not exist")
    void getUserByIdCase2() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }
}
