package com.ridemates.app.passenger;

import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.passenger.domain.Passenger;
import com.ridemates.app.passenger.infrastructure.PassengerRepository;
import com.ridemates.app.utilis.Reader;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PassengerControllerTest {

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    Reader reader;

    @Autowired
    private MockMvc mockMvc;

    String token = "";
    Long id;

    @BeforeEach
    public void setUpPassenger() throws Exception {

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

        Passenger passenger = passengerRepository.findByEmail("nayeli.guzman@utec.edu.pe")
                .orElseThrow(() -> new NotFoundException("Passenger", "nayeli.guzman@utec.edu.pe", "email"));

        id = passenger.getId();

        JSONObject login = new JSONObject(Objects.requireNonNull(response.getResponse().getContentAsString()));
        token = login.getString("token");

        System.out.println("Token: "+ token);

    }

    @Test
    @WithMockUser(roles = "DRIVER")
    public void testDriverGetPassengerById () throws Exception {
        mockMvc.perform(get("/passenger/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testAnonymousGetPassengerById () throws Exception {
        mockMvc.perform(get("/passenger/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void testPassengerGetPassengerById () throws Exception {
        mockMvc.perform(get("/passenger/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


}
