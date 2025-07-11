package com.ridemates.app.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequestDto {

    @NotNull
    private Long routeId;
    @NotNull
    private Long passengerId;
    @NotNull
    private String origin;
    @NotNull
    private String destination;
    @NotNull
    private Double price;

}
