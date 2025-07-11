package com.ridemates.app.stripe.dto;

import lombok.Data;

@Data
public class StripeRequest {
    private Long travelId;
    private String email;
    private Double amount;

    public long amountInCents() {
        return Math.round(amount * 100L);
    }
}
