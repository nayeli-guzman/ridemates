package com.ridemates.app.payment.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDto {
    private String method;
    @NotNull
    private Double amount;
}