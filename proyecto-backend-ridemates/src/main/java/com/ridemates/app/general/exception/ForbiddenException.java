package com.ridemates.app.general.exception;

import lombok.experimental.StandardException;

@StandardException
public class ForbiddenException extends RuntimeException {
    String entity;
    Object id;

    public ForbiddenException(String entity, Object id, String type) {
        super(entity + " associated with " + type + ": " + id + " has not yet been verified");
        this.entity = entity;
        this.id = id;
    }
}
