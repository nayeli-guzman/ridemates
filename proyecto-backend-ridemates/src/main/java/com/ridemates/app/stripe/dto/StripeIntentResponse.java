package com.ridemates.app.stripe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeIntentResponse {
    private String clientSecret;
}
