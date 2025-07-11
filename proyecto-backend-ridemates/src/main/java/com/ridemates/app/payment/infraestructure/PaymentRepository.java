package com.ridemates.app.payment.infraestructure;

import com.ridemates.app.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByTravel_Booking_Passenger_Id(Long passengerId, Pageable pageable);
    Page<Payment> findByTravel_Booking_Passenger_Email(String mail, Pageable pageable);

    Optional<Payment> findByTravel_Id(Long travelId);
}