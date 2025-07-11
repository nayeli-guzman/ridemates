package com.ridemates.app.user.dto;

import com.ridemates.app.user.domain.Gender;
import com.ridemates.app.vehicle.dto.VehicleRequestDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Getter
@Setter
public class RegisterUserDto {
   @NotNull
   String firstName;
   @NotNull
   String lastName;
   @NotNull
   Gender gender;
   @Email
   String email;
   @Size(min=8)
   String password;
   @Size(min=9, max = 9)
   String phone;
   @NotNull
   LocalDate birthDate;
   @NotNull
   Boolean isDriver;

   // Driver attributes here
   String license;
   VehicleRequestDto vehicle;

}
