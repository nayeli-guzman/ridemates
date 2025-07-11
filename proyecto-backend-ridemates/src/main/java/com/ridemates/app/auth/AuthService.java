package com.ridemates.app.auth;

import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.email.domain.EmailService;
import com.ridemates.app.email.event.ValidateEvent;
import com.ridemates.app.general.exception.*;
import com.ridemates.app.passenger.domain.Passenger;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.user.domain.User;
import com.ridemates.app.user.dto.LogInUserDto;
import com.ridemates.app.user.dto.RegisterUserDto;
import com.ridemates.app.user.dto.VerifyUserDto;
import com.ridemates.app.user.infrastructure.UserRepository;
import com.ridemates.app.vehicle.domain.Vehicle;
import com.ridemates.app.vehicle.dto.VehicleRequestDto;
import com.ridemates.app.vehicle.infrastructure.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class AuthService implements ApplicationEventPublisherAware {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public User createUser(RegisterUserDto userDto) {
        boolean conf = userRepository.findByEmail(userDto.getEmail()).isPresent();
        if (conf) throw new ConflictException("User", userDto.getEmail(), "email");

        User user;
        String encode = userDto.getPassword() == null ? null : passwordEncoder.encode(userDto.getPassword());
        if (userDto.getIsDriver()) {
            VehicleRequestDto vehicleRequestDto = userDto.getVehicle();
            Vehicle vehicle = new Vehicle(
                    vehicleRequestDto.getPlate(),
                    vehicleRequestDto.getCapacity(),
                    vehicleRequestDto.getSoat(),
                    vehicleRequestDto.getModel()
            );
            vehicle.setCreatedAt(LocalDate.now());
            vehicle = vehicleRepository.save(vehicle);

            Driver driver = new Driver(
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    userDto.getPhone(),
                    userDto.getGender(),
                    encode,
                    userDto.getLicense(),
                    LocalDate.now(),
                    userDto.getBirthDate(),
                    vehicle
            );
            user = driver;
            vehicle.setDriver(driver);
        }
        else {
            user = new Passenger(
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    userDto.getPhone(),
                    userDto.getGender(),
                    LocalDate.now(),
                    userDto.getBirthDate(),
                    encode
            );
            user.setRole(Role.PASSENGER);
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        return user;
    }

    public User createUser(RegisterUserDto userDto, Consumer<User> preSave) {
        User user = createUser(userDto);
        preSave.accept(user);
        return user;
    }

    public VerifyUserDto signup(RegisterUserDto userDto) {
        var user = createUser(userDto);
        publisher.publishEvent(new ValidateEvent(user.getEmail(), user));
        // revisar si se agregarà al usuario antes de ingresar el code de verificación


        user = userRepository.save(user);
        return modelMapper.map(user, VerifyUserDto.class);
    }

    public User authenticate(LogInUserDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(()-> new NotFoundException("User", input.getEmail(), "email"));

        if (!user.isEnabled()) {
            throw new ForbiddenException("User", input.getEmail(), "email");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return user;
    }

    public void verifyUser(VerifyUserDto input) {

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(()-> new NotFoundException("User", input.getEmail(), "email"));

        LocalDateTime verificationCodeExpiresAt = user.getVerificationCodeExpiresAt();
        if (verificationCodeExpiresAt == null) {
            throw new ConflictException("User", "anonymous", input.getEmail());
        }

        if (verificationCodeExpiresAt.isBefore(LocalDateTime.now())) {
            throw new GoneException("Verification code", user.getVerificationCode());
        }
        if (user.getVerificationCode().equals(input.getVerificationCode())) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            user.setVerificationCodeExpiresAt(null);
            userRepository.save(user);
        } else {
            throw new NoAuthException("Verification code", user.getVerificationCode());
        }

    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User", email, "email"));

        if (user.isEnabled()) {
            throw new RuntimeException("Account is already verified");
        }
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        //publisher.publishEvent(new ValidateEvent(user.getEmail(), user));
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000)+ 100000;
        //return "1";
        return String.valueOf(code);
    }


}
