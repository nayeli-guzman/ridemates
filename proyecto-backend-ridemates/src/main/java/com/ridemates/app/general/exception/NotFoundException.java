package com.ridemates.app.general.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Object id;
    private final String subject;

    public NotFoundException(String subject,
                             Object id,
                             String name) {
        super(subject + " with " +  name + " " + id + " not found");
        this.id = id;
        this.subject = subject;
    }
}
