package com.ridemates.app.route.domain;

import com.google.common.base.Objects;
import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.ors.domain.GeoLocation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER)
    private List<Booking> bookings;

    /*
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "origin_id", nullable = false)
    private GeoLocation origin;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private GeoLocation destination;
    */

    @Column
    private String origin;

    @Column
    private String destination;

    @Column
    private int capacity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(nullable = false)
    private ZonedDateTime dateTime;

    public Route(String origin,
                 String destination,
                 int capacity,
                 Driver driver,
                 ZonedDateTime dateTime
    ) {
        this.origin = origin;
        this.destination = destination;
        this.capacity = capacity;
        this.driver = driver;
        this.dateTime = dateTime;
        this.bookings = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", origin=" + origin +
                ", destination=" + destination +
                ", capacity=" + capacity +
                ", driver=" + driver +
                ", dateTime=" + dateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route route)) return false;
        return capacity == route.capacity && Objects.equal(id, route.id)
                //&& Objects.equal(origin.getId(), route.origin.getId())
                //&& Objects.equal(destination.getId(), route.destination.getId())
                && Objects.equal(driver.getId(), route.driver.getId())
                && Objects.equal(dateTime, route.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id,
                                //origin.getId(),
                                //destination.getId(),
                                capacity, driver.getId(), dateTime);
    }
}
