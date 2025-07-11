package com.ridemates.app.driver.application;

import com.ridemates.app.driver.domain.DriverService;
import com.ridemates.app.driver.dto.DriverResponseDto;
import com.ridemates.app.vehicle.dto.VehicleRequestDto;
import com.ridemates.app.vehicle.dto.VehicleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService service;

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDto> getMethod(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdResponse(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DriverResponseDto>> getAllDrivers() {
        return ResponseEntity.ok(service.findAllResponse());
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/me")
    public ResponseEntity<DriverResponseDto> getMe() {
        return ResponseEntity.ok(service.findMe());
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PatchMapping("/me/vehicle")
    public ResponseEntity<VehicleResponseDto> update(@RequestBody VehicleRequestDto dto) {
        var update = service.changeMyVehicle(dto);
        return ResponseEntity.ok(update);
    }
}
