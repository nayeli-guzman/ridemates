package com.ridemates.app.travel.domain;

import com.google.common.base.Objects;
import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.events.TravelChangeStatusEvent;
import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.payment.domain.Payment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;

/**
 * Travel of a {@link com.ridemates.app.passenger.domain.Passenger}
 * on a {@link com.ridemates.app.route.domain.Route}.
 */
@Entity
@Setter
@Getter
public class Travel {

    public enum TravelStatus {
        BUCKET, WAITING, RUNNING, FINISH, CANCELLED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double distance;
    @Column(nullable = false)
    private LocalDate createdAt;
    /*
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private GeoLocation destination;
     */

    @Column
    private String destination;

    @Column(nullable = false)
    private TravelStatus travelStatus;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @OneToOne
    private Payment payment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Travel travel)) return false;
        return Objects.equal(id, travel.id)
                && Objects.equal(distance, travel.distance)
                && Objects.equal(createdAt, travel.createdAt)
                && Objects.equal(destination, travel.destination)
                && travelStatus == travel.travelStatus
                && Objects.equal(payment, travel.payment);
    }

    public void setTravelStatus(TravelStatus travelStatus) {
        // publisher.publishEvent(new TravelChangeStatusEvent(this, this.travelStatus, travelStatus));
        this.travelStatus = travelStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, distance, createdAt, destination, travelStatus, payment);
    }
}
