package com.ridemates.app.passenger.application;

import com.ridemates.app.passenger.domain.PassengerService;
import com.ridemates.app.passenger.dto.PassengerResponseDto;
import com.ridemates.app.user.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @GetMapping("{id}")
    public ResponseEntity<PassengerResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.findByIdResponse(id));
    }


}
