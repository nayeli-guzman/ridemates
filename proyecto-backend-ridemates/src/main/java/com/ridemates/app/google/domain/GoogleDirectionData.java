package com.ridemates.app.google.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoogleDirectionData {
    private final BigDecimal price;
    private final double durationSeconds;
}
