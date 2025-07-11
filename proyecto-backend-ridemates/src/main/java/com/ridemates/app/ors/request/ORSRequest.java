package com.ridemates.app.ors.request;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpRequest;

/**
 * Interface for OpenRouteService API requests.
 * Contains his own logic to map itself as a {@link HttpRequest}.
 * @see ORSRequest#toHttpRequest(ObjectMapper)
 */
public interface ORSRequest {

    default HttpRequest.Builder toHttpRequest(ObjectMapper objectMapper) {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }
}
