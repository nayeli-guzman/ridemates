package com.ridemates.app.booking.dto;

import com.ridemates.app.booking.domain.Booking;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private String driver;
    private String passenger;
    private ZonedDateTime dateTime;
    private Booking.Status status;
}
