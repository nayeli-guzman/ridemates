package com.ridemates.app.booking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.AbstractContainerBaseTest;
import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.booking.infrastructure.BookingRepository;
import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.geolocation.infrastructure.GeoLocationRepository;
import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.passenger.domain.Passenger;
import com.ridemates.app.passenger.infrastructure.PassengerRepository;
import com.ridemates.app.route.domain.Route;
import com.ridemates.app.route.infrastructure.RouteRepository;
import com.ridemates.app.user.domain.Gender;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.utilis.Reader;
import com.ridemates.app.vehicle.domain.Vehicle;
import com.ridemates.app.vehicle.infrastructure.VehicleRepository;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest{

    @Autowired
    DriverRepository driverRepository;
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    Reader reader;
    @Autowired
    private MockMvc mockMvc;

    Driver driver;
    Passenger passenger;
    Long routeId;
    String driverToken="", passengerToken="";
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp() throws Exception {
        bookingRepository.deleteAll();
        driverRepository.deleteAll();
        passengerRepository.deleteAll();
        routeRepository.deleteAll();
        createDriver();
        createPassenger();
        createRoute();
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void bookingByPassenger() throws Exception {

        String jsonContent = Reader.readJsonFile("/booking/post.json");
        jsonContent = reader.updateRouteId(jsonContent, "routeId", routeId);
        jsonContent = reader.updatePassengerId(jsonContent, "passengerId", passenger.getId());

        var res = mockMvc.perform(post("/booking/book")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                    .header("Authorization", "Bearer " + passengerToken))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    @WithMockUser(roles = "DRIVER")
    public void bookingByDriver() throws Exception {

        String jsonContent = Reader.readJsonFile("/booking/post.json");
        jsonContent = reader.updateRouteId(jsonContent, "routeId", routeId);
        jsonContent = reader.updatePassengerId(jsonContent, "passengerId", driver.getId());

        var res = mockMvc.perform(post("/booking/book")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                        .header("Authorization", "Bearer " + driverToken))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithAnonymousUser
    public void bookingByAnonymous() throws Exception {

        String jsonContent = Reader.readJsonFile("/booking/post.json");
        jsonContent = reader.updateRouteId(jsonContent, "routeId", routeId);


        var res = mockMvc.perform(post("/booking/book")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void deleteBookingByPassenger() throws Exception {

        Long idBooking = createBooking();
        mockMvc.perform(delete("/booking/{idBooking}", idBooking)
                .header("Authorization", "Bearer " + passengerToken)
                )
                .andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(roles = "DRIVER")
    public void deleteBookingByDriver() throws Exception {

        Long idBooking = createBooking();
        mockMvc.perform(delete("/booking/{idBooking}", idBooking)
                        .header("Authorization", "Bearer " + driverToken)
                )
                .andExpect(status().isForbidden());

    }

    @Test
    @WithAnonymousUser
    public void deleteBookingByAnonymous() throws Exception {

        Long idBooking = createBooking();
        mockMvc.perform(delete("/booking/{idBooking}", idBooking)
                )
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "DRIVER", value = "salvador.donayre@utec.edu.pe")
    public void changeStatusByDriver() throws Exception {

        Long id = createBooking();
        String jsonContent = Reader.readJsonFile("/booking/patchStatus.json");

        var res = mockMvc.perform(patch("/booking/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                        .header("Authorization", "Bearer " + driverToken))
                .andExpect(status().isOk())
                .andReturn();

        Booking booking = bookingRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Booking", id, "id")
        );

        Assertions.assertEquals(Booking.Status.ACCEPTED, booking.getStatus());
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void changeStatusByPassenger() throws Exception {
        Long id = createBooking();
        String jsonContent = Reader.readJsonFile("/booking/patchStatus.json");

        var res = mockMvc.perform(patch("/booking/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                        .header("Authorization", "Bearer " + passengerToken))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @WithAnonymousUser
    public void changeStatusByAnonymous() throws Exception {
        Long id = createBooking();
        String jsonContent = Reader.readJsonFile("/booking/patchStatus.json");

        var res = mockMvc.perform(patch("/booking/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    public void createDriver() throws Exception {

        driverRepository.deleteAll();
        String jsonContent = Reader.readJsonFile("/auth/postDriver.json");

        var res = mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        String verificationCodeDriver = jsonObject.getString("verificationCode");

        String jsonVerify = Reader.readJsonFile("/auth/verifyDriver.json");
        jsonVerify = reader.updateVerificationCode(jsonVerify, "verificationCode", verificationCodeDriver);

        mockMvc.perform(post("/auth/verify")
                        .contentType(APPLICATION_JSON)
                        .content(jsonVerify))
                .andExpect(status().isOk());

        String jsonLogin = Reader.readJsonFile("auth/loginDriver.json");
        var response = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn();

        driver = driverRepository.findByEmail("salvador.donayre@utec.edu.pe")
                .orElseThrow(() -> new NotFoundException("Driver", "salvador.donayre@utec.edu.pe", "email"));


        JSONObject login = new JSONObject(Objects.requireNonNull(response.getResponse().getContentAsString()));
        driverToken = login.getString("token");

    }

    public void createPassenger() throws Exception {

        passengerRepository.deleteAll();
        String jsonContent = Reader.readJsonFile("/auth/postPassenger.json");

        var res = mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        String verificationCodePassenger = jsonObject.getString("verificationCode");

        String jsonVerify = Reader.readJsonFile("/auth/verifyPassenger.json");
        jsonVerify = reader.updateVerificationCode(jsonVerify, "verificationCode", verificationCodePassenger);

        mockMvc.perform(post("/auth/verify")
                        .contentType(APPLICATION_JSON)
                        .content(jsonVerify))
                .andExpect(status().isOk());

        String jsonLogin = Reader.readJsonFile("auth/loginPassenger.json");
        var response = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn();

        passenger = passengerRepository.findByEmail("nayeli.guzman@utec.edu.pe")
                .orElseThrow(() -> new NotFoundException("Passenger", "nayeli.guzman@utec.edu.pe", "email"));

        JSONObject login = new JSONObject(Objects.requireNonNull(response.getResponse().getContentAsString()));
        passengerToken = login.getString("token");

    }

    public void createRoute() throws Exception {

        String routeRequest = Reader.readJsonFile("/route/post.json");

        var res = mockMvc.perform(post("/route/driver")
                        .contentType(APPLICATION_JSON)
                        .content(routeRequest)
                        .with(user("salvador.donayre@utec.edu.pe").roles("DRIVER"))
                        .header("Authorization", "Bearer " + driverToken))
                .andExpect(status().isCreated()).andReturn();

        String locationHeader = res.getResponse().getHeader("Location");
        String[] parts = locationHeader.split("/");
        routeId = Long.parseLong(parts[parts.length - 1]);

    }

    public Long createBooking() throws Exception {
        String jsonContent = Reader.readJsonFile("/booking/post.json");
        jsonContent = reader.updateRouteId(jsonContent, "routeId", routeId);
        jsonContent = reader.updatePassengerId(jsonContent, "passengerId", passenger.getId());

        var res = mockMvc.perform(post("/booking/book")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                        .with(user("nayeli.guzman@utec.edu.pe").roles("PASSENGER"))
                        .header("Authorization", "Bearer " + passengerToken))
                .andExpect(status().isCreated())
                .andReturn();
        String locationHeader = res.getResponse().getHeader("Location");
        String[] parts = locationHeader.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

}
