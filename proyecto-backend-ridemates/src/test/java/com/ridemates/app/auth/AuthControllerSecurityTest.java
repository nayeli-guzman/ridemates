package com.ridemates.app.auth;

import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.passenger.domain.Passenger;
import com.ridemates.app.passenger.infrastructure.PassengerRepository;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.utilis.Reader;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WithUserDetails(value = "adrian.montes@utec.edu.pe", setupBefore = TestExecutionEvent.TEST_EXECUTION)
//@WithMockUser(roles = "DRIVER")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    Reader reader;

    String verificationCodeDriver = "", verificationCodePassenger = "",
            tokenDriver = "", tokenPassenger = "";
    @Autowired
    private PassengerRepository passengerRepository;

    @BeforeEach
    public void setUpPassenger() throws Exception {

        passengerRepository.deleteAll();
        String jsonContent = Reader.readJsonFile("/auth/postPassenger.json");

        var res = mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        verificationCodePassenger = jsonObject.getString("verificationCode");
        System.out.println("Verification code: " + verificationCodeDriver);
    }

    @BeforeEach
    public void setUpDriver() throws Exception {

        driverRepository.deleteAll();
        String jsonContent = Reader.readJsonFile("/auth/postDriver.json");

        var res = mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        verificationCodeDriver = jsonObject.getString("verificationCode");
        System.out.println("Verification code: " + verificationCodeDriver);

    }

    @Test
    public void testVerificationCodeDriver() throws Exception {

        String jsonContent = Reader.readJsonFile("/auth/postDriver.json");
        jsonContent = reader.updateVerificationCode(jsonContent, "verificationCode", verificationCodeDriver);

        var res = mockMvc.perform(post("/auth/verify")
                .contentType(APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());

        Driver driver = driverRepository.findByEmail("salvador.donayre@utec.edu.pe")
                .orElseThrow(() -> new NotFoundException("Driver", "salvador.donayre@utec.edu.pe", "email"));

        Assertions.assertEquals(driver.isEnabled(), true);
        Assertions.assertEquals(driver.getVerificationCodeExpiresAt(), null);
        Assertions.assertEquals(driver.getRole(), Role.DRIVER);
    }

    @Test
    public void testLoginDriver() throws Exception {
        String jsonContent = Reader.readJsonFile("/auth/verifyDriver.json");
        jsonContent = reader.updateVerificationCode(jsonContent, "verificationCode", verificationCodeDriver);

        mockMvc.perform(post("/auth/verify")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());

        String jsonLogin = Reader.readJsonFile("auth/loginDriver.json");

        var res = mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        tokenDriver = jsonObject.getString("token");
    }

    @Test
    public void testVerificationCodePassenger() throws Exception {

        String jsonContent = Reader.readJsonFile("/auth/verifyPassenger.json");
        jsonContent = reader.updateVerificationCode(jsonContent, "verificationCode", verificationCodePassenger);

        var res = mockMvc.perform(post("/auth/verify")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());

        Passenger passenger = passengerRepository.findByEmail("nayeli.guzman@utec.edu.pe")
                .orElseThrow(() -> new NotFoundException("Driver", "nayeli.guzman@utec.edu.pe", "email"));

        Assertions.assertEquals(passenger.isEnabled(), true);
        Assertions.assertEquals(passenger.getVerificationCodeExpiresAt(), null);
        Assertions.assertEquals(passenger.getRole(), Role.PASSENGER);
    }

    @Test
    public void testLoginPassenger() throws Exception {
        String jsonContent = Reader.readJsonFile("/auth/postPassenger.json");
        jsonContent = reader.updateVerificationCode(jsonContent, "verificationCode", verificationCodePassenger);

        mockMvc.perform(post("/auth/verify")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());

        String jsonLogin = Reader.readJsonFile("auth/loginPassenger.json");

        var res = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        tokenPassenger = jsonObject.getString("token");
    }

}
