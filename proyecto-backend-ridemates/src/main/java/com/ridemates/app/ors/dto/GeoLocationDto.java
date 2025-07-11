package com.ridemates.app.ors.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeoLocationDto {
    @NotNull
    private Double longitude;
    @NotNull
    private Double latitude;
}
