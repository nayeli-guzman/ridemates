package com.ridemates.app.ors.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ors")
@Data
public class ORSProperties {
    private String apiKey;
}
