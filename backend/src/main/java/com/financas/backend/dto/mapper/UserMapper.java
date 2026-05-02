package com.financas.backend.dto.mapper;

import com.financas.backend.dto.response.UserResponseDTO;
import com.financas.backend.entity.User;

public class UserMapper {

    public static UserResponseDTO mapUserToUserResponseDTO(User user){
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
}
