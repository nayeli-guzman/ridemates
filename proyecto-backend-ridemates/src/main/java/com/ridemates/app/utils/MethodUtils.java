package com.ridemates.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

public interface MethodUtils {


    @SuppressWarnings("unchecked")
    static <T> T patch(T entity, JsonPatch patch, ObjectMapper mapper) {
        JsonNode mapped = mapper.convertValue(entity, JsonNode.class);
        try {
            patch.apply(mapped);
            return (T) mapper.treeToValue(mapped, entity.getClass());
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
