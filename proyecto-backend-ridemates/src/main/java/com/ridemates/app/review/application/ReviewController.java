package com.ridemates.app.review.application;

import com.ridemates.app.review.domain.ReviewService;
import com.ridemates.app.review.dto.ReviewRequestDto;
import com.ridemates.app.review.dto.ReviewResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ReviewResponseDto createdReview = reviewService.createReview(username, reviewRequestDto);
        return ResponseEntity.ok(createdReview);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByUser(@PathVariable Long userId, @RequestParam int page, @RequestParam int size) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByUser(userId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewService.deleteReview(username, reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByDriver(@PathVariable Long driverId, @RequestParam int page, @RequestParam int size) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByDriver(driverId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByPassenger(@PathVariable Long passengerId, @RequestParam int page, @RequestParam int size) {
        Page<ReviewResponseDto> reviews = reviewService.getReviewsByPassenger(passengerId, page, size);
        return ResponseEntity.ok(reviews);
    }
}