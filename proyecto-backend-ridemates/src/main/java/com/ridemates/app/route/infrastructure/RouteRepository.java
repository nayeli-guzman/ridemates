package com.ridemates.app.route.infrastructure;

import com.ridemates.app.route.domain.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<List<Route>> findAllByDriverId(Long id);
    Page<Route> findAll(Pageable pageable);
}
