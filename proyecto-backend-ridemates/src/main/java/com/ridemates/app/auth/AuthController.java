package com.ridemates.app.auth;

import com.ridemates.app.jwt.JwtService;
import com.ridemates.app.user.domain.User;
import com.ridemates.app.user.dto.LogInUserDto;
import com.ridemates.app.user.dto.LoginResponse;
import com.ridemates.app.user.dto.RegisterUserDto;
import com.ridemates.app.user.dto.VerifyUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<VerifyUserDto> register (@RequestBody RegisterUserDto registerUserDto) {
        VerifyUserDto registeredUser = authService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate (@RequestBody LogInUserDto logInUserDto) {
        User autheticatedUser = authService.authenticate(logInUserDto);
        String jwtToken = jwtService.generateToken(autheticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        authService.verifyUser(verifyUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendVerificationCde(@RequestParam String email) {
        authService.resendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }
}
