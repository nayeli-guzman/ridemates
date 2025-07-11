package com.ridemates.app.ors.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ridemates.app.ors.OrsAPI;
import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.ors.dto.GeoLocationDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class DirectionPostRequest implements ORSRequest {
    @NotNull
    private OrsAPI.Profile profile;
    @NotNull
    private List<GeoLocationDto> coordinates;
    @NotNull
    private String units;

    @Override
    public HttpRequest.Builder toHttpRequest(ObjectMapper objectMapper) {
        ArrayNode coordinatesNode = objectMapper.createArrayNode();
        for (GeoLocationDto coordinate : coordinates) {
            coordinatesNode = coordinatesNode.add(objectMapper.createArrayNode()
                                    .add(coordinate.getLongitude())
                                    .add(coordinate.getLatitude())
            );
        }

        ObjectNode body = objectMapper.createObjectNode()
                .set("coordinates", coordinatesNode);
        body.put("units", units);
        return ORSRequest.super.toHttpRequest(objectMapper)
                .uri(URI.create(OrsAPI.ORS_DIRECTIONS + profile.asText()))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()));
    }
}
