package com.ridemates.app.events;

import org.springframework.context.ApplicationEvent;

public class DriverUpdatePosEvent extends ApplicationEvent {
    public DriverUpdatePosEvent(Object source) {
        super(source);
    }
}
