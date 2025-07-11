package com.ridemates.app.vehicle.dto;

import com.ridemates.app.driver.domain.Driver;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleResponseDto {
    private String model;
    private String plate;
    private Integer capacity;
    private Long driverId;
    private LocalDate createdAt;
}
