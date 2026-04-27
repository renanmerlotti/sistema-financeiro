package com.financas.backend.dto.mapper;

import com.financas.backend.dto.request.UserRequestDTO;
import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;

public class UserMapper {

    public static UserResponseDTO mapUserToUserResponseDTO(User user){
        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }

    public static User mapUserRegistrationDTOtoUser(UserRequestDTO userRequestDTO){
        User user = new User();

        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());

        return user;
    }
}
