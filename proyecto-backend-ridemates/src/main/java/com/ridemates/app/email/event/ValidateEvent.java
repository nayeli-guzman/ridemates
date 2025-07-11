package com.ridemates.app.email.event;

import com.ridemates.app.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Setter
@Getter
public class ValidateEvent extends ApplicationEvent {

    private String email;
    private User user;
    
    public ValidateEvent(String email, User user) {
        super(email);
        this.email = email;
        this.user = user;
    }

}
