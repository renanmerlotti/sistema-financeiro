package com.financas.backend.controller;

import com.financas.backend.dto.request.UserRegistrationDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO){
        UserResponseDTO savedUser = userService.createUser(userRegistrationDTO);

        return ResponseEntity.ok(savedUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long userId) {
        UserResponseDTO userResponseDTO = userService.getUserById(userId);

        return ResponseEntity.ok(userResponseDTO);
    }
}
