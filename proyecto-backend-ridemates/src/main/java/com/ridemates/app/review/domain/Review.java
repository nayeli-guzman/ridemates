package com.ridemates.app.review.domain;

import com.ridemates.app.user.domain.Role;
import com.ridemates.app.travel.domain.Travel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @Column(nullable = false)
    private Role roleType;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Long dirigidoA;

    @ManyToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;
}