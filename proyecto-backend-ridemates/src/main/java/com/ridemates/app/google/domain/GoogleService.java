package com.ridemates.app.google.domain;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.ridemates.app.auth.AuthService;
import com.ridemates.app.general.exception.NoAuthException;
import com.ridemates.app.jwt.JwtService;
import com.ridemates.app.price.PriceFormula;
import com.ridemates.app.user.domain.User;
import com.ridemates.app.user.domain.UserService;
import com.ridemates.app.user.dto.LoginResponse;
import com.ridemates.app.user.dto.RegisterUserDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class GoogleService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthService authService;

    private final PriceFormula priceFormula;
    private final GeoApiContext apiContext;

    public GoogleService(UserService userService, JwtService jwtService, AuthService authService, PriceFormula priceFormula, GeoApiContext apiContext) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authService = authService;
        this.priceFormula = priceFormula;
        this.apiContext = apiContext;
    }

    public GoogleResponseDto validateRequest(GoogleRequestDto dto) {
        var verifier = TokenVerifier.newBuilder().build();

        try {
            JsonWebSignature jsonWebSignature = verifier.verify(dto.getAccessToken());
            jsonWebSignature.getPayload();
            return GoogleResponseDto.yes();
        } catch (Exception e) {
            return GoogleResponseDto.no();
        }
    }

    public LoginResponse signin(GoogleRequestDto dto) {
        var verifier = TokenVerifier.newBuilder().build();

        System.out.println("signin = " + dto);
        try {
            // Verificando el token (tira exception en caso
            JsonWebSignature jsonWebSignature = verifier.verify(dto.getAccessToken());

            // Desde el payload, obtener usuario by email
            JsonWebToken.Payload payload = jsonWebSignature.getPayload();
            String email = payload.get("email").toString();
            System.out.println("email = " + email);
            User byEmail = userService.findByEmail(email);

            // Todo OK, crear auth token
            return new LoginResponse(jwtService.generateToken(byEmail), jwtService.getExpirationTime());
        } catch (TokenVerifier.VerificationException e) {
            throw new NoAuthException("Invalid token");
        }
    }

    public LoginResponse signup(GoogleSignupDto dto) throws TokenVerifier.VerificationException {
        var verifier = TokenVerifier.newBuilder().build();
        System.out.println("dto = " + dto);

        JsonWebSignature jsonWebSignature = verifier.verify(dto.getCredential());
        JsonWebToken.Payload payload = jsonWebSignature.getPayload();

        boolean isVerified = String.valueOf(payload.get("email_verified")).equalsIgnoreCase("true");
        if (!isVerified) {
            throw new NoAuthException("Email not verified");
        }

        String email = (String) payload.get("email");
        // Object picture = payload.get("picture");
        Object givenName = payload.get("given_name");
        Object familyName = payload.get("family_name");

        RegisterUserDto userDto = new RegisterUserDto();

        userDto.setEmail(email);
        userDto.setFirstName(givenName.toString());
        if (familyName != null) {
            userDto.setLastName(familyName.toString());
        } else {
            userDto.setLastName(""); // XD
        }
        userDto.setGender(dto.getGender());
        userDto.setPhone(dto.getPhone());
        userDto.setLicense(dto.getLicense());
        userDto.setBirthDate(dto.getBirthDate());
        userDto.setIsDriver(dto.isDriver());
        userDto.setVehicle(dto.getVehicle());
        userDto.setPassword(null);

        User user = authService.createUser(userDto,
                newUser -> newUser.setGoogleClientUserId(payload.get("sub").toString()));

        return new LoginResponse(jwtService.generateToken(user), jwtService.getExpirationTime());
    }

    public GoogleDirectionData calculatePriceAndTime(String origin, String destination) {
        DirectionsApiRequest directions = DirectionsApi.getDirections(
                apiContext,
                origin,
                destination
        );

        try {

            DirectionsResult await = CompletableFuture.completedFuture(directions.await())
                    .orTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                    //.exceptionally(_ -> null)
                    .join();
            if (await == null) {
                return null;
            }

            DirectionsRoute route = await.routes[0];
            DirectionsLeg leg = route.legs[0];
            Distance distance = leg.distance;
            return new GoogleDirectionData(
                    new BigDecimal(distance.inMeters / 500.f),
                    (double) leg.duration.inSeconds / 60
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
