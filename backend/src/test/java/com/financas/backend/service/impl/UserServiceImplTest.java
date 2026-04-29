package com.financas.backend.service.impl;

import com.financas.backend.dto.request.UserRequestDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.exception.ConflictException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    @DisplayName("Should return UserResponseDTO when user is created")
    void createUserCase1() {
        UserRequestDTO dto = new UserRequestDTO("joao", "joao@email.com", "123456");
        User savedUser = new User(1L, "joao", "joao@email.com", "123456");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("joao", result.getUsername());
    }

    @Test
    @DisplayName("Should throw ConflictException when email is already in use")
    void createUserCase2() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserRequestDTO userRequestDTO = new UserRequestDTO("renan", "renan@email.com", "213241241");

        assertThrows(ConflictException.class, () -> userService.createUser(userRequestDTO));
    }

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
    void getUserByIdCase2(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }
}