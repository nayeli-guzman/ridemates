package com.ridemates.app.review.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.booking.infrastructure.BookingRepository;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.passenger.domain.Passenger;
import com.ridemates.app.passenger.infrastructure.PassengerRepository;
import com.ridemates.app.review.dto.ReviewRequestDto;
import com.ridemates.app.review.dto.ReviewResponseDto;
import com.ridemates.app.review.infrastructure.ReviewRepository;
import com.ridemates.app.travel.domain.Travel;
import com.ridemates.app.travel.infrastructure.TravelRepository;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.user.domain.User;
import com.ridemates.app.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService extends GenericService<Review, ReviewResponseDto, Long, ReviewRepository> {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TravelRepository travelRepository;
    private final PassengerRepository passengerRepository;
    @Autowired
    BookingRepository bookingsRepository;

    @Autowired
    public ReviewService(ReviewRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper, UserRepository userRepository, TravelRepository travelRepository, PassengerRepository passengerRepository) {
        super("Review", repository, objectMapper, modelMapper, Review.class, ReviewResponseDto.class);
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.travelRepository = travelRepository;
        this.passengerRepository = passengerRepository;
    }

    public ReviewResponseDto createReview(String username, ReviewRequestDto reviewRequestDto) {
        // saca el email al que lo solicita
        System.out.println("Creando revisión...");

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User", username, "email"));

        Travel travel = travelRepository.findById(reviewRequestDto.getTravelId())
                .orElseThrow(() -> new NotFoundException("Travel", reviewRequestDto.getTravelId(), "id"));

        Booking booking = travel.getBooking();

        System.out.println("Detalles de usuario y viaje obtenidos.");

        Long dirigidoA; // para quiene es el comment

        if (user.getRole() == Role.PASSENGER) {
            System.out.println("El usuario es un PASSENGER.");
            dirigidoA = booking.getRoute().getDriver().getId(); // get id de driver

        } else if (user.getRole() == Role.DRIVER) {
            System.out.println("El usuario es un DRIVER.");

            dirigidoA = reviewRequestDto.getPassengerId();

            // Obtener el Passenger utilizando passengerId
            Passenger passenger = passengerRepository.findById(reviewRequestDto.getPassengerId())
                    .orElseThrow(() -> new NotFoundException("Passenger", reviewRequestDto.getPassengerId(), "id"));

            // Validar que el passenger forma parte de la reserva y que el estado es ACCEPTED
            boolean isPassengerInRoute = passenger.getBookings().contains(travel.getBooking())
                    && booking.getStatus() == Booking.Status.ACCEPTED;

            if (!isPassengerInRoute) {
                System.out.println("El pasajero no está en la misma ruta que el conductor o el estado de la reserva no es ACCEPTED.");
                throw new IllegalArgumentException("El pasajero no está en la misma ruta que el conductor o el estado de la reserva no es ACCEPTED");
            }
        } else {
            System.out.println("Rol de usuario inválido.");
            throw new IllegalArgumentException("Rol de usuario inválido");
        }
        System.out.println("Procediendo a crear la revisión.");

        Review review = new Review();
        review.setComment(reviewRequestDto.getComment());
        review.setRoleType(reviewRequestDto.getRoleType());
        review.setScore(reviewRequestDto.getScore());
        review.setTravel(travel);
        review.setDirigidoA(dirigidoA);

        review = repository.save(review);
        System.out.println("Revisión creada exitosamente.");
        return modelMapper.map(review, ReviewResponseDto.class);
    }


    public Page<ReviewResponseDto> getReviewsByUser(Long userId, int page, int size) {
        return repository.findByDirigidoA(userId, Pageable.ofSize(page).withPage(size))
                .map(review -> modelMapper.map(review, ReviewResponseDto.class));
    }

    public Page<ReviewResponseDto> getReviewsByDriver(Long driverId, int page, int size) {
        return repository.findByDirigidoA(driverId, Pageable.ofSize(page).withPage(size))
                .map(review -> modelMapper.map(review, ReviewResponseDto.class));
    }

    public Page<ReviewResponseDto> getReviewsByPassenger(Long passengerId, int page, int size) {
        return repository.findByDirigidoA(passengerId, Pageable.ofSize(page).withPage(size))
                .map(review -> modelMapper.map(review, ReviewResponseDto.class));
    }

    public void deleteReview(String username, Long reviewId) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User", username, "email"));
        Review review = repository.findById(reviewId).orElseThrow(() -> new NotFoundException("Review", reviewId, "id"));

        if (!review.getTravel().getBooking().getPassenger().getId().equals(user.getId()) &&
                !review.getTravel().getBooking().getRoute().getDriver().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not authorized to delete this review");
        }

        repository.delete(review);
    }
}