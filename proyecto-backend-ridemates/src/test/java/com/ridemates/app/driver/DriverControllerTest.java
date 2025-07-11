package com.ridemates.app.driver;


import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.utilis.Reader;
import com.ridemates.app.vehicle.domain.VehicleService;
import com.ridemates.app.vehicle.dto.VehicleRequestDto;
import com.ridemates.app.vehicle.infrastructure.VehicleRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DriverControllerTest {

    @Autowired
    DriverRepository driverRepository;
    @Autowired
    Reader reader;
    @Autowired
    private MockMvc mockMvc;

    String token = "";
    Long id;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setUpDriver() throws Exception {

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

        Driver driver = driverRepository.findByEmail("salvador.donayre@utec.edu.pe")
                .orElseThrow(() -> new NotFoundException("Driver", "salvador.donayre@utec.edu.pe", "email"));

        id = driver.getId();

        JSONObject login = new JSONObject(Objects.requireNonNull(response.getResponse().getContentAsString()));
        token = login.getString("token");

        System.out.println("Token: "+ token);

    }


    @Test
    @WithMockUser(roles = "PASSENGER")
    public void testPassengerGetDriverById () throws Exception {
        mockMvc.perform(get("/driver/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    public void testDriverGetDriverById () throws Exception {
        mockMvc.perform(get("/driver/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testAnonymousGetDriverById () throws Exception {
        mockMvc.perform(get("/driver/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void testPassengerGetDrivers() throws Exception {
        mockMvc.perform(get("/driver/all")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "DRIVER")
    public void testDriverGetDrivers () throws Exception {
        mockMvc.perform(get("/driver/all")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testAnonymousGetDrivers() throws Exception {
        mockMvc.perform(get("/driver/all")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void testOwnInfoPassenger() throws Exception {
        mockMvc.perform(get("/driver/me")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DRIVER", value = "salvador.donayre@utec.edu.pe")
    public void testOwnInfoDriver() throws Exception {
        mockMvc.perform(get("/driver/me")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testOwnInfoAnonymous() throws Exception {
        mockMvc.perform(get("/driver/me")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "PASSENGER")
    public void testGetOwnVehiclePassenger() throws Exception {

        String jsonContent = Reader.readJsonFile("vehicle/post.json");

        mockMvc.perform(patch("/driver/me/vehicle")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        vehicleRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "DRIVER", value = "salvador.donayre@utec.edu.pe")
    public void testGetOwnVehicleDriver() throws Exception {
        String jsonContent = Reader.readJsonFile("vehicle/post.json");

        var res = mockMvc.perform(patch("/driver/me/vehicle")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();

        JSONObject login = new JSONObject(Objects.requireNonNull(res.getResponse().getContentAsString()));
        String plate = login.getString("plate");

        Assertions.assertEquals(plate, "20001");

        vehicleRepository.deleteAll();


    }

    @Test
    @WithAnonymousUser
    public void testOwnVehicleAnonymous() throws Exception {
        String jsonContent = Reader.readJsonFile("vehicle/post.json");

        mockMvc.perform(patch("/driver/me/vehicle")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        vehicleRepository.deleteAll();

    }


}
