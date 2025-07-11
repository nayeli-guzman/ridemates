package com.ridemates.app.review.dto;

import com.ridemates.app.user.domain.Role;
import lombok.Data;

@Data
public class ReviewResponseDto {
    private Long id;
    private String comment;
    private Role roleType;
    private Integer score;
    private Long travelId;
    private Long dirigidoA; // Campo dirigidoA
}