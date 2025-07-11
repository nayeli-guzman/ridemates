package com.ridemates.app.route.dto;

import com.ridemates.app.ors.dto.GeoLocationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
public class RouteRequestDto {
    @NotNull
    private String origin;
    @NotNull
    private String destination;
    @NotNull
    private int capacity;
    @NotNull
    private ZonedDateTime dateTime;
}
