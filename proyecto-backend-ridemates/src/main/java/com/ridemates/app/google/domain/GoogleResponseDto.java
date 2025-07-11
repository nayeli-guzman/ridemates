package com.ridemates.app.google.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleResponseDto {
    private boolean valid;

    public static GoogleResponseDto yes() {
        return new GoogleResponseDto(true);
    }

    public static GoogleResponseDto no() {
        return new GoogleResponseDto(false);
    }
}
