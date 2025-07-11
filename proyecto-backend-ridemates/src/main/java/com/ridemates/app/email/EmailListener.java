package com.ridemates.app.email;

import com.ridemates.app.email.domain.EmailService;
import com.ridemates.app.email.event.DeleteBookingEvent;
import com.ridemates.app.email.event.StatusBookingEvent;
import com.ridemates.app.email.event.ValidateEvent;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailListener {

    @Autowired
    private EmailService emailService;


    @EventListener
    @Async
    public void handleRegistrationEvent(
            ValidateEvent event
    ) throws MessagingException {
        emailService.sendValidateEmail(
                event.getEmail(), event.getUser()
        );
    }

    @EventListener
    @Async
    public void handleDeleteBooking(
            DeleteBookingEvent event
    ) throws MessagingException {
        emailService.sendDeleteBookingEmail(
                event.getBooking()
        );
    }

    @EventListener
    @Async
    public void handleStatusBookingEvent(
            StatusBookingEvent event
    ) throws MessagingException {
        emailService.sendStatusBookingEmail(
                event.getBooking()
        );
    }

}
