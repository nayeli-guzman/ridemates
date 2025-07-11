package com.ridemates.app.payment.application;

import com.ridemates.app.payment.domain.PaymentService;
import com.ridemates.app.payment.dto.PaymentRequestDto;
import com.ridemates.app.payment.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/me")
    public ResponseEntity<Page<PaymentResponseDto>> getMyPayments(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(paymentService.getMyPayments(page, size));
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/travel/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentByTravelId(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentByTravelId(id));
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByUserId(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        Page<PaymentResponseDto> payments = paymentService.getPaymentsByUserId(id, page, size);
        return ResponseEntity.ok(payments);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/{travel_id}")
    public ResponseEntity<PaymentResponseDto> createPayment(@PathVariable Long travel_id,
                                                            @RequestBody PaymentRequestDto paymentRequestDto) {
        var created = paymentService.createPayment(travel_id, paymentRequestDto, null);
        return ResponseEntity.created(URI.create("/payment/" + created.id())).body(created.entity());
    }
}