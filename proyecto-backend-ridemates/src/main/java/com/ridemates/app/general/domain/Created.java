package com.ridemates.app.general.domain;

public record Created<T, I>(T entity, I id) {

    public static <T1, I1>  Created<T1, I1> of(T1 entity, I1 id) {
        return new Created<>(entity, id);
    }
}
