package com.ridemates.app.travel.dto;

import lombok.Data;

@Data
public class TravelRequestDto {
    private Long bookingId; // By booking, get route and calculate distance, destination, etc
}
