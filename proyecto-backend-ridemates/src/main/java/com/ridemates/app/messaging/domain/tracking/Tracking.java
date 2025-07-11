package com.ridemates.app.messaging.domain.tracking;

import com.ridemates.app.ors.domain.GeoLocation;
import lombok.Data;

@Data
public class Tracking {
    protected String driverUsername;
    protected GeoLocation currentPos;
}
