package com.financas.backend.service.impl;

import com.financas.backend.dto.request.LoginRequestDTO;
import com.financas.backend.dto.request.UserRequestDTO;
import com.financas.backend.dto.response.AuthResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.exception.ConflictException;
import com.financas.backend.repository.UserRepository;
import com.financas.backend.security.JwtService;
import com.financas.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Email already in use");
        }

        User user = new User();
        user.setName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        return new AuthResponseDTO(jwtService.generateToken(user));
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        return new AuthResponseDTO(jwtService.generateToken(user));
    }
}
