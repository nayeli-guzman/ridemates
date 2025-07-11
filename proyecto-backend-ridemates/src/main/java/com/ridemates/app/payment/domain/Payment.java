package com.ridemates.app.payment.domain;

import com.ridemates.app.travel.domain.Travel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Double amount;

    @OneToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    private String paymentIntentId;
}
