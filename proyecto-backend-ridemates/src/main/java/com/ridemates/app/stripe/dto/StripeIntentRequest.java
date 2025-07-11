package com.ridemates.app.stripe.dto;

import lombok.Data;

@Data
public class StripeIntentRequest {
    private Float amount;

    public long amountInCents() {
        return Math.round(amount * 100L);
    }
}
