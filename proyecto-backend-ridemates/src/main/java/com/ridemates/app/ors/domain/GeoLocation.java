package com.ridemates.app.ors.domain;

import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.ors.dto.GeoLocationDto;
import com.ridemates.app.route.domain.Route;
import com.ridemates.app.travel.domain.Travel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * A Geographic location of two dimensions.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "geolocations")
public class GeoLocation {

    public static final GeoLocationDto UTEC_GEO_LOCATION = new GeoLocationDto(-77.02237889055853,-12.13554371385455);
    public static final GeoLocationDto KENNEDY_GEO_LOCATION = new GeoLocationDto(-77.02893703950741,-12.119796519087584);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Route> originRoutes = new HashSet<>();

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Route> destinationRoutes = new HashSet<>();

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Travel> destinationTravels = new HashSet<>();

    public GeoLocation(Double longitude, Double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoLocationDto toDto() {
        return new GeoLocationDto(longitude, latitude);
    }
}