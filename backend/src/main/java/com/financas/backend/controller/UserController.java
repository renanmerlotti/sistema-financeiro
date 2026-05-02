package com.financas.backend.controller;

import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long userId) {
        UserResponseDTO userResponseDTO = userService.getUserById(userId);

        return ResponseEntity.ok(userResponseDTO);
    }
}
