package com.ridemates.app.booking.dto;

import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.dto.DriverResponseDto;
import com.ridemates.app.route.domain.Route;
import com.ridemates.app.route.dto.RouteResponseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingIntentDto {
    private Long id;
    private Long driverId;
    private RouteResponseDto route;
    private String origin;
    private String destination;
    private BigDecimal price;
    private double timeEstimated;
}
