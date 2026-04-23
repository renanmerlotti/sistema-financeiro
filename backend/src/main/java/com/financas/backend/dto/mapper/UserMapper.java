package com.financas.backend.dto.mapper;

import com.financas.backend.dto.request.UserRegistrationDTO;
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

    public static User mapUserRegistrationDTOtoUser(UserRegistrationDTO userRegistrationDTO){
        User user = new User();

        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(userRegistrationDTO.getPassword());

        return user;
    }
}
