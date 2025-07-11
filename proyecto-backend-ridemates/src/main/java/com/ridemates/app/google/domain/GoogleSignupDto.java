package com.ridemates.app.google.domain;

import com.ridemates.app.user.domain.Gender;
import com.ridemates.app.vehicle.dto.VehicleRequestDto;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GoogleSignupDto {
    private String credential;
    private String clientId;

    @Size(min=9, max = 9)
    private String phone;
    private Gender gender;
    private LocalDate birthDate;

    private boolean isDriver;
    private String license;
    private VehicleRequestDto vehicle;
}
