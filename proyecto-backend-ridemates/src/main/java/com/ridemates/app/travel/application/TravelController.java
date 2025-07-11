package com.ridemates.app.travel.application;

import com.ridemates.app.passenger.dto.PassengerResponseDto;
import com.ridemates.app.travel.domain.TravelService;
import com.ridemates.app.travel.dto.TravelResponseDto;
import com.ridemates.app.travel.dto.TravelStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/travel")
public class TravelController {

    private final TravelService travelService;

    @GetMapping("/{id}")
    public ResponseEntity<TravelResponseDto> getTravel(@PathVariable Long id) {
        return ResponseEntity.ok(travelService.findId(id));
    }

    @GetMapping("/{id}/passengers")
    public ResponseEntity<Page<PassengerResponseDto>> passengersOf(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(travelService.findAllPassengersOfTravel(id, page, size));
    }

    @GetMapping("/{id}/state")
    public ResponseEntity<TravelStatusDto> stateOf(@PathVariable Long id) {
        return ResponseEntity.ok(travelService.findStatusOfTravel(id));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<Void> changeStateOf(@PathVariable Long id, @RequestBody TravelStatusDto status) {
        travelService.updateState(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<Page<TravelResponseDto>> allTravelsOf(@PathVariable Long id, @RequestParam int size, @RequestParam int page) {
        return ResponseEntity.ok(travelService.findAllTravelsOfDriver(id, page, size));
    }

    @GetMapping("/driver/me")
    public ResponseEntity<Page<TravelResponseDto>> allTravelsOfMe(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(travelService.findAllTravelsOfMe(page, size));
    }
}
