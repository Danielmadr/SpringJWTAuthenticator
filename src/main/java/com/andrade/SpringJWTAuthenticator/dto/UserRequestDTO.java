package com.andrade.SpringJWTAuthenticator.dto;


import com.andrade.SpringJWTAuthenticator.model.enums.UserStatus;

public record UserRequestDTO(
        String username,
        String email,
        String password,
        UserStatus status
) {
}
