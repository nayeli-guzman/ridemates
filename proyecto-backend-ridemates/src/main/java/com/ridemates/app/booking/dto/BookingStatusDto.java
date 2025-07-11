package com.ridemates.app.booking.dto;

import com.ridemates.app.booking.domain.Booking;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingStatusDto {
    Booking.Status status;
}
