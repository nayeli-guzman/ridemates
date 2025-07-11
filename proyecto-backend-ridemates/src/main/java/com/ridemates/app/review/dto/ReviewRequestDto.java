package com.ridemates.app.review.dto;

import com.ridemates.app.user.domain.Role;
import lombok.Data;

@Data
public class ReviewRequestDto {
    private String comment;
    private Role roleType; // de quien viene rol
    private Integer score;
    private Long travelId;
    private Long passengerId; // Solo para el conductor que califica al pasajero
    private Long dirigidoA; // Campo dirigidoA
}