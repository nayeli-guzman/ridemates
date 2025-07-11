package com.ridemates.app.travel.dto;

import com.ridemates.app.travel.domain.Travel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TravelStatusDto {
    private Travel.TravelStatus travelStatus;
}
