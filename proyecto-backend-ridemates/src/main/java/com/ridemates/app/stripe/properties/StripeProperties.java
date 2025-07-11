package com.ridemates.app.stripe.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("stripe")
@Data
public class StripeProperties {
    private String secretKey;
    private String publicKey;
}
