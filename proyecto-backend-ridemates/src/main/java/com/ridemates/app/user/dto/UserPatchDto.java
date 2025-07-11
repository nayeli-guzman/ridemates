package com.ridemates.app.user.dto;

import com.ridemates.app.user.domain.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserPatchDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Gender gender;
    private String phone;
    private Integer calification;
    private LocalDate birthDate;
}
