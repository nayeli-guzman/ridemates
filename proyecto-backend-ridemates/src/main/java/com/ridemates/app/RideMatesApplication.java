package com.ridemates.app;

import com.ridemates.app.ors.properties.ORSProperties;
import com.ridemates.app.stripe.properties.StripeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties({ORSProperties.class, StripeProperties.class})
public class RideMatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideMatesApplication.class, args);
    }

}
