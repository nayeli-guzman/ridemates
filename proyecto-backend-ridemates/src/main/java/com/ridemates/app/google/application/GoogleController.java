package com.ridemates.app.google.application;

import com.google.auth.oauth2.TokenVerifier;
import com.ridemates.app.google.domain.GoogleRequestDto;
import com.ridemates.app.google.domain.GoogleResponseDto;
import com.ridemates.app.google.domain.GoogleService;
import com.ridemates.app.google.domain.GoogleSignupDto;
import com.ridemates.app.user.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/google")
public class GoogleController {

    @Autowired
    private GoogleService googleService;

    @GetMapping("/validate")
    public ResponseEntity<GoogleResponseDto> validateRequest(@RequestBody GoogleRequestDto dto) {
        return ResponseEntity.ok(googleService.validateRequest(dto));
    }

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@RequestBody GoogleSignupDto dto) throws TokenVerifier.VerificationException {
        return ResponseEntity.ok(googleService.signup(dto));
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signin(@RequestBody GoogleRequestDto dto) {
        return ResponseEntity.ok(googleService.signin(dto));
    }
}
