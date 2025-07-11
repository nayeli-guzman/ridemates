package com.ridemates.app.driver.dto;

import com.ridemates.app.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequestDto {

    @NotNull
    String license;

    @NotNull
    Long idVehicle;

}
