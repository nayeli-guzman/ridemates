package com.ridemates.app.route.dto;


import com.ridemates.app.ors.dto.GeoLocationDto;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class RouteResponseDto {
    private Long id;
    private String origin, destination;
    private int capacity;
    private ZonedDateTime dateTime;
    private String driverFullName;
}
