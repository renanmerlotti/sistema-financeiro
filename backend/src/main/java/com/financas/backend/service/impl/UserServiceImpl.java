package com.financas.backend.service.impl;

import com.financas.backend.dto.mapper.UserMapper;
import com.financas.backend.dto.request.UserRegistrationDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;
import com.financas.backend.repository.UserRepository;
import com.financas.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRegistrationDTO userRegistrationDTO) {
        User user = UserMapper.mapUserRegistrationDTOtoUser(userRegistrationDTO);
        User savedUser = userRepository.save(user);

        return UserMapper.mapUserToUserResponseDTO(savedUser);
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map((user) -> UserMapper.mapUserToUserResponseDTO(user));
    }


}
