package com.ridemates.app.stripe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeResponse {
    private String paymentIntent;
    private String ephemeralKey;
    private String customer;
    private String publishableKey;

    private String paymentMethod;

}
