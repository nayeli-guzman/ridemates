package com.ridemates.app.travel.dto;

import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.ors.dto.GeoLocationDto;
import com.ridemates.app.travel.domain.Travel;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TravelResponseDto {
    private Double distance;
    private LocalDate createdAt;
    private String destination;
    private Travel.TravelStatus travelStatus;
    private Long bookingId;
    private Long paymentId;
}
