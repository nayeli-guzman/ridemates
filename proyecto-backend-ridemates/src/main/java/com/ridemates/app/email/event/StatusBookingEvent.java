package com.ridemates.app.email.event;

import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.passenger.domain.Passenger;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StatusBookingEvent extends ApplicationEvent {
    Booking booking;

    public StatusBookingEvent(Booking booking) {
        super(booking.getPassenger().getEmail());
        this.booking = booking;
    }

}
