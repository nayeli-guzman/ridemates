package com.ridemates.app.payment.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class PaymentResponseDto {
    private Long travelId;
    private String method;
    private Double amount;
    private ZonedDateTime createdAt;
    private String paymentIntentId;
}