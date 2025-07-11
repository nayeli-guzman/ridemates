package com.ridemates.app.route.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.ridemates.app.geolocation.domain.GeoLocationService;
import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.ors.domain.ORSService;
import com.ridemates.app.ors.dto.GeoLocationDto;
import com.ridemates.app.route.domain.Route;
import com.ridemates.app.route.domain.RouteService;
import com.ridemates.app.route.dto.RouteRequestDto;
import com.ridemates.app.route.dto.RouteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/route")
public class RouteController {

    private final GeoLocationService service;
    private final RouteService routeService;
    private final ORSService orsService;

    @GetMapping("/all")
    public ResponseEntity<Page<RouteResponseDto>> getAllPosts(@RequestParam int page, @RequestParam int size) {
        Page<RouteResponseDto> response = routeService.getAllRoutes(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/")
    public ResponseEntity<RouteResponseDto> findByLoc(@RequestBody GeoLocationDto location) throws IOException, InterruptedException {
        JsonNode jsonNode = orsService.matrixDistance(GeoLocation.UTEC_GEO_LOCATION, location);

        // FIXME: algorithm to search best route

        return null;
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/driver")
    public ResponseEntity<RouteResponseDto> createRoute(
            @RequestBody RouteRequestDto dto
    ) throws URISyntaxException {
        var created = routeService.createRoute(dto);
        URI location = new URI("/route/driver/" + created.id());
        return ResponseEntity.created(location).body(created.entity());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findRoute(id));
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<List<RouteResponseDto>> findByDriverId(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findAllRoutesOfDriver(id));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/driver/me")
    public ResponseEntity<List<RouteResponseDto>> findByMe() {
        return ResponseEntity.ok(routeService.findAllRoutesOfMe());
    }
}
