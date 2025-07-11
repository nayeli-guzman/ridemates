package com.ridemates.app.email.event;


import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.passenger.domain.Passenger;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Setter
@Getter
public class DeleteBookingEvent extends ApplicationEvent {

    Passenger passenger;
    Booking booking;

    public DeleteBookingEvent(Passenger passenger, Booking booking) {
        super(passenger.getEmail());
        this.passenger = passenger;
        this.booking = booking;
    }

}
