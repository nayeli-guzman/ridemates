package com.ridemates.app.vehicle.application;

import com.ridemates.app.driver.dto.DriverResponseDto;
import com.ridemates.app.vehicle.domain.Vehicle;
import com.ridemates.app.vehicle.domain.VehicleService;
import com.ridemates.app.vehicle.dto.VehicleResponseDto;
import com.ridemates.app.vehicle.infrastructure.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity<List<VehicleResponseDto>> allVehicles() {
        return ResponseEntity.ok(vehicleService.findAllResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findByIdResponse(id));
    }

    @GetMapping("/{id}/driver")
    public ResponseEntity<DriverResponseDto> getDriver(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findDriverOf(id));
    }
}
