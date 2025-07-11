package com.ridemates.app.driver.dto;

import com.ridemates.app.vehicle.dto.VehicleResponseDto;
import lombok.Data;

@Data
public class DriverResponseDto {
    private String license;
    private VehicleResponseDto vehicle;
    private Integer travelCount;
    private Double wallet;
}
