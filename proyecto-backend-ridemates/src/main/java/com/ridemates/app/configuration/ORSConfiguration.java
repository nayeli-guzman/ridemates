package com.ridemates.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class ORSConfiguration {

    @Bean
    public HttpClient providesClient() {
        return HttpClient.newHttpClient();
    }
}
