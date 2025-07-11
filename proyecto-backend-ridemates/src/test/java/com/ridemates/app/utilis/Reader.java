package com.ridemates.app.utilis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class Reader {

    @Autowired
    ObjectMapper mapper;

    public static String readJsonFile(String filePath) throws IOException {
        File resource = new ClassPathResource(filePath).getFile();
        byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }

    public String updateVerificationCode(String json, String key, String verificationCode) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);
        ((ObjectNode) jsonNode).put(key, verificationCode);
        return mapper.writeValueAsString(jsonNode);
    }
    public String updateRouteId(String json, String key, Long verificationCode) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);
        ((ObjectNode) jsonNode).put(key, verificationCode);
        return mapper.writeValueAsString(jsonNode);
    }

    public String updatePassengerId(String json, String key, Long verificationCode) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);
        ((ObjectNode) jsonNode).put(key, verificationCode);
        return mapper.writeValueAsString(jsonNode);
    }
    public String updateId(String json, String key, Long id) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);

        ((ObjectNode) jsonNode).put(key, id);

        return mapper.writeValueAsString(jsonNode);
    }

    public String updateDriverEmail(String json, String key, String email) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);

        ((ObjectNode) jsonNode).put(key, email);

        return mapper.writeValueAsString(jsonNode);
    }

    public String updateVehicleLicensePlate(String json, String key, String newLicensePlate) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);
        ((ObjectNode) jsonNode.get("vehicle")).put(key, newLicensePlate);

        return mapper.writeValueAsString(jsonNode);
    }

    public String updateReviewRelatioshipsId(String json, String key, Long id) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(json);

        ((ObjectNode) jsonNode).put(key, id);

        return mapper.writeValueAsString(jsonNode);
    }

}
