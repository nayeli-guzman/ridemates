package com.ridemates.app.user.dto;

import com.ridemates.app.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Email
    private String email;
    @NotNull
    private Role role;
    @NotNull
    private Integer calification;
    @NotNull
    private LocalDate createdAt;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private Integer phone;
}
