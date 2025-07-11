package com.ridemates.app.route;

import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.general.exception.NotFoundException;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class RouteControllerTest {

    @Autowired
    DriverRepository driverRepository;
    @Autowired
    Reader reader;
    @Autowired
    private MockMvc mockMvc;

    String token = "";
    Long driverId;

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

        driverId = driver.getId();

        JSONObject login = new JSONObject(Objects.requireNonNull(response.getResponse().getContentAsString()));
        token = login.getString("token");

        System.out.println("Token: "+ token);

    }

    @Test
    @WithMockUser(roles = "PASSENGER", value = "nayeli.guzman@utec.edu.pe")
    public void testCreateRouteByPassenger() throws  Exception{
        String routeRequest = Reader.readJsonFile("/route/post.json");

        mockMvc.perform(post("/route/driver")
                        .contentType(APPLICATION_JSON)
                        .content(routeRequest)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DRIVER", value = "salvador.donayre@utec.edu.pe")
    public void testCreateRouteByDriver() throws  Exception{
        String routeRequest = Reader.readJsonFile("/route/post.json");

        var res = mockMvc.perform(post("/route/driver")
                        .contentType(APPLICATION_JSON)
                        .content(routeRequest)
                        .header("Authorization", "Bearer " + token))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.origin.latitude").value(12000.0))
                .andExpect(jsonPath("$.origin.longitude").value(12000.0))
                .andExpect(jsonPath("$.destination.latitude").value(13000.0))
                .andExpect(jsonPath("$.destination.longitude").value(13000.0))
                .andExpect(jsonPath("$.capacity").value(3));
    }

    @Test
    @WithAnonymousUser
    public void testCreateRouteByAnonymous() throws  Exception{
        String routeRequest = Reader.readJsonFile("/route/post.json");

        mockMvc.perform(post("/route/driver")
                        .contentType(APPLICATION_JSON)
                        .content(routeRequest)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "PASSENGER", value = "salvador.donayre@utec.edu.pe")
    public void testCreateRouteByWrongPassenger() throws  Exception{
        String routeRequest = Reader.readJsonFile("/route/post.json");

        mockMvc.perform(post("/route/driver")
                        .contentType(APPLICATION_JSON)
                        .content(routeRequest)
                )
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = "DRIVER", value = "salvador.donayre@utec.edu.pe")
    public void testDriverFindByDriverId() throws Exception {

        String routeRequest = Reader.readJsonFile("/route/post.json");

        var res = mockMvc.perform(post("/route/driver")
                        .contentType(APPLICATION_JSON)
                        .content(routeRequest)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/route/driver/{driverId}", driverId)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].origin.latitude").value(12000.0))
                .andExpect(jsonPath("$[0].origin.longitude").value(12000.0))
                .andExpect(jsonPath("$[0].destination.latitude").value(13000.0))
                .andExpect(jsonPath("$[0].destination.longitude").value(13000.0))
                .andExpect(jsonPath("$[0].capacity").value(3))
                .andExpect(jsonPath("$[0].dateTime").value("2000-01-02T00:00:00Z"))
                .andExpect(jsonPath("$[0].driverId").value(driverId));
    }

    @Test
    public void testPassengerFindByDriverId() throws Exception {

        String routeRequest = Reader.readJsonFile("/route/post.json");

        var res = mockMvc.perform(post("/route/driver")
                        .contentType(APPLICATION_JSON)
                        .content(routeRequest)
                        .with(user("salvador.donayre@utec.edu.pe").roles("DRIVER"))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/route/driver/{driverId}", driverId)
                        .contentType(APPLICATION_JSON)
                        .with(user("passenger").roles("PASSENGER"))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].origin.latitude").value(12000.0))
                .andExpect(jsonPath("$[0].origin.longitude").value(12000.0))
                .andExpect(jsonPath("$[0].destination.latitude").value(13000.0))
                .andExpect(jsonPath("$[0].destination.longitude").value(13000.0))
                .andExpect(jsonPath("$[0].capacity").value(3))
                .andExpect(jsonPath("$[0].dateTime").value("2000-01-02T00:00:00Z"))
                .andExpect(jsonPath("$[0].driverId").value(driverId));
    }

    @Test
    @WithAnonymousUser
    public void testAnonymousFindByDriverId() throws Exception {

        mockMvc.perform(get("/route/driver/{driverId}", driverId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "DRIVER", value = "salvador.donayre@utec.edu.pe")
    public void testOwnRoutesByDriver() throws Exception {
        mockMvc.perform(get("/route/driver/me")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PASSENGER")
    public void testOwnRoutesByPassenger() throws Exception {
        mockMvc.perform(get("/route/driver/me")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void testOwnRoutesByAnonymous() throws Exception {
        mockMvc.perform(get("/route/driver/me")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
