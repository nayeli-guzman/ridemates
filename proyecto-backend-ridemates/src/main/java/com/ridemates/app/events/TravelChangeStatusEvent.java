package com.ridemates.app.events;

import com.ridemates.app.travel.domain.Travel;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TravelChangeStatusEvent extends ApplicationEvent {
    private final Travel.TravelStatus oldStatus;
    private final Travel.TravelStatus newStatus;

    public TravelChangeStatusEvent(Object source, Travel.TravelStatus oldStatus, Travel.TravelStatus newStatus) {
        super(source);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}
