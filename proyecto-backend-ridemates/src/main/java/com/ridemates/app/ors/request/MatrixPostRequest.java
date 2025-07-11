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
public class MatrixPostRequest implements ORSRequest {
    public enum Metrics {
        DISTANCE, DURATION
    }

    @NotNull
    private OrsAPI.Profile profile;
    @NotNull
    private List<GeoLocationDto> points;
    private List<Integer> destinations;
    private List<Integer> sources;
    private Metrics[] metrics;
    private Boolean resolveLocations;
    private String id;

    @Override
    public HttpRequest.Builder toHttpRequest(ObjectMapper objectMapper) {
        ObjectNode body = objectMapper.createObjectNode();

        ArrayNode locationsNode = objectMapper.createArrayNode();
        points.forEach(point -> locationsNode.add(objectMapper.createArrayNode()
                        .add(point.getLongitude())
                        .add(point.getLatitude())
                )
        );

        body = body.set("locations", locationsNode);

        if (destinations != null) {
            ArrayNode destinationsNode = objectMapper.createArrayNode();
            destinations.forEach(destinationsNode::add);
            body = body.set("destinations", destinationsNode);
        }

        if (sources != null) {
            ArrayNode sourcesNode = objectMapper.createArrayNode();
            sources.forEach(sourcesNode::add);
            body = body.set("sources", sourcesNode);
        }


        if (metrics != null) {
            ArrayNode metricsNode = objectMapper.createArrayNode();
            for (Metrics metric : metrics) {
                metricsNode.add(metric.name().toLowerCase());
            }

            body = body.set("metrics", metricsNode);
        }

        if (id != null) {
            body.put("id", id);
        }

        if (resolveLocations != null) {
            body.put("resolve_locations", resolveLocations);
        }

        return ORSRequest.super.toHttpRequest(objectMapper)
                .uri(URI.create(OrsAPI.ORS_DISTANCE_MATRIX + profile.asText()))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()));
    }
}
