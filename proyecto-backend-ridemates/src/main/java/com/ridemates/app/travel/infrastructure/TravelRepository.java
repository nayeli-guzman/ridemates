package com.ridemates.app.travel.infrastructure;

import com.ridemates.app.travel.domain.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    Page<Travel> findAllByBooking_Route_Driver_email(String driver_email, Pageable pageable);

    Page<Travel> findAllByBooking_Route_Driver_id(Long driver_id, Pageable pageable);
}
