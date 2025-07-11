package com.ridemates.app.configuration;

/*
import com.google.maps.GeoApiContext;*/
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.ridemates.app.price.PriceFormula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Configuration
public class GoogleMapsConfig {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;


    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey(googleMapsApiKey)
                .build();
    }

    @Bean
    public PriceFormula providesPriceFormula(GeoApiContext context) {
        return (origin, destination) -> {
            DirectionsApiRequest directions = DirectionsApi.getDirections(
                    context,
                    origin,
                    destination
            );

            try {
                DirectionsResult await = CompletableFuture.completedFuture(directions.await())
                        .orTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                        //.exceptionally(_ -> null)
                        .join();
                if (await == null) {
                    return BigDecimal.ZERO;
                }

                DirectionsRoute route = await.routes[0];
                DirectionsLeg leg = route.legs[0];
                Distance distance = leg.distance;
                return new BigDecimal(distance.inMeters / 500.f);
            } catch (Exception e) {
                return BigDecimal.ZERO;
            }
        };
    }
}
