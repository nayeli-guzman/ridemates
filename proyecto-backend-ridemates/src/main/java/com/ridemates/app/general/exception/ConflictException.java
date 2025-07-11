package com.ridemates.app.general.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

    private final String email;
    private final String subject;

    public ConflictException(String subject, String email, String name) {
        super(subject + " with " + name + " " + email + " already exists");
        this.email = email;
        this.subject = subject;
    }
}
