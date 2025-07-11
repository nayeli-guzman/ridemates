package com.ridemates.app.ors.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.ors.OrsAPI;
import com.ridemates.app.ors.dto.GeoLocationDto;
import com.ridemates.app.ors.request.DirectionPostRequest;
import com.ridemates.app.ors.request.MatrixPostRequest;
import com.ridemates.app.ors.request.ORSRequest;
import com.ridemates.app.ors.properties.ORSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Service
public class ORSService {

    @Autowired
    private ORSProperties orsProperties;
    @Autowired
    private HttpClient client;
    @Autowired
    private ObjectMapper objectMapper;

    public JsonNode findDirectionBy(GeoLocationDto start, GeoLocationDto end) throws IOException, InterruptedException {
        return sendRequest(new DirectionPostRequest(OrsAPI.Profile.DRIVING_CAR, List.of(start, end), OrsAPI.UNIT_KM));
    }

    public JsonNode matrixDistance(GeoLocationDto... points) throws IOException, InterruptedException {
        return sendRequest(new MatrixPostRequest(OrsAPI.Profile.DRIVING_CAR,
                Arrays.asList(points), null, null, MatrixPostRequest.Metrics.values(), true, ""));
    }

    public JsonNode sendRequest(ORSRequest request) throws IOException, InterruptedException {
        HttpRequest toSend = request.toHttpRequest(objectMapper)
                .header("Authorization", orsProperties.getApiKey())
                .build();
        var response = client.send(
                toSend,
                HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }
}
