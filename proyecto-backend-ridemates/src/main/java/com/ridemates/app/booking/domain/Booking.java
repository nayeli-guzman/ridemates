package com.ridemates.app.booking.domain;

import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.passenger.domain.Passenger;
import com.ridemates.app.route.domain.Route;
import com.ridemates.app.travel.domain.Travel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.Destination;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
public class Booking {

    public enum Status {
        PENDING, ACCEPTED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    // from foreign key of Travel
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Travel travel;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Status status;

    public Booking(Passenger passenger,
                   Route route,
                   Double price
    ) {
        this.passenger = passenger;
        this.route = route;
        this.createdAt = ZonedDateTime.now();
        this.status = Status.PENDING;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", passenger=" + passenger.getId() +
                ", travel=" + (travel == null ? null : travel.getId()) +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking booking)) return false;
        return Objects.equals(id, booking.id) && Objects.equals(createdAt, booking.createdAt) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, route, createdAt, status);
    }
}