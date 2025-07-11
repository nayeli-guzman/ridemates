package com.ridemates.app.geolocation.infrastructure;


import com.ridemates.app.ors.domain.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoLocationRepository extends JpaRepository<GeoLocation, Long> {
}
