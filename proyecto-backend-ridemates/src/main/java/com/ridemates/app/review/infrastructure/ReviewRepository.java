package com.ridemates.app.review.infrastructure;

import com.ridemates.app.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTravel_Booking_Passenger_Id(Long passengerId);
    List<Review> findByTravel_Booking_Route_Driver_Id(Long driverId);
    Page<Review> findByDirigidoA(Long dirigidoA, Pageable pageable);
}