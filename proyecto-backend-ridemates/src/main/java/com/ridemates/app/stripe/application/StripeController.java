package com.ridemates.app.stripe.application;

import com.ridemates.app.stripe.domain.StripeService;
import com.ridemates.app.stripe.dto.StripeIntentRequest;
import com.ridemates.app.stripe.dto.StripeIntentResponse;
import com.ridemates.app.stripe.dto.StripeRequest;
import com.ridemates.app.stripe.dto.StripeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripe")
public class StripeController {
    @Autowired
    StripeService service;

    @PostMapping("/payment-sheet")
    public ResponseEntity<StripeResponse> payment(@RequestBody StripeRequest request) throws Exception {
        return ResponseEntity.ok(service.generatePayment(request));
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<StripeIntentResponse> intent(@RequestBody StripeIntentRequest request) throws Exception {
        return ResponseEntity.ok(service.intentPayment(request));
    }
}
