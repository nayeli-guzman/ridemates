package com.ridemates.app.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInUserDto {
    @Email
    private String email;
    @NotNull
    private String password;
}
