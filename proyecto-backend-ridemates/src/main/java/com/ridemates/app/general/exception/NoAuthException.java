package com.ridemates.app.general.exception;

import lombok.experimental.StandardException;

@StandardException
public class NoAuthException extends RuntimeException {

    String entity;
    Object id;

    public NoAuthException(String entity, Object id) {
        super(entity + ": " + id + " es incorrecto ");
    }
}
