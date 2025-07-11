package com.ridemates.app.booking.application;

import com.ridemates.app.booking.domain.BookingService;
import com.ridemates.app.booking.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/book")
    public ResponseEntity<BookingResponseDto> book(
            @RequestBody BookingRequestDto bookingRequestDto
    ) throws URISyntaxException {
        BookingResponseDto booking = bookingService.book(bookingRequestDto);
        URI location = new URI("/booking/book/" + booking.getId());
        return ResponseEntity.created(location).body(booking);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/{id}/intent")
    public ResponseEntity<BookingIntentDto> bookCalculate(
            @PathVariable Long id,
            @RequestBody BookingCalculateDto bookingRequestDto
    ) throws URISyntaxException {
        BookingIntentDto booking = bookingService.intent(id, bookingRequestDto);
        URI location = new URI("/booking/book/" + booking.getId());
        return ResponseEntity.created(location).body(booking);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('DRIVER') and @bookingService.canSetStatus(#id, authentication.name)")
    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDto> changeStatusBooking(
            @PathVariable Long id,
            @RequestBody BookingStatusDto status ) {
        BookingResponseDto booking = bookingService.setStatus(id, status);
        return ResponseEntity.ok(booking);
    }

    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/me")
    public ResponseEntity<Page<BookingResponseDto>> getMyBookings(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(page, size));
    }

}
