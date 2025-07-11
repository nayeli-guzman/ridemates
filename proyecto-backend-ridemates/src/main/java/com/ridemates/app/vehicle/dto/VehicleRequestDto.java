package com.ridemates.app.vehicle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class VehicleRequestDto {
    @NotNull
    public String plate;
    @NotNull
    public Integer capacity;
    @NotNull
    public String soat;
    @NotNull
    public String model;
}
