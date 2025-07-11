package com.ridemates.app.general.exception;

import lombok.Getter;

public class GoneException extends RuntimeException {
    private final Object id;
    private final String subject;

    public GoneException(String subject,
                             Object id) {
        super(subject + ": " + id + " has already expired");
        this.id = id;
        this.subject = subject;
    }
}
