package com.ridemates.app.booking.domain;

import com.amazonaws.services.apigatewayv2.model.RouteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.booking.dto.*;
import com.ridemates.app.booking.infrastructure.BookingRepository;
import com.ridemates.app.driver.dto.DriverResponseDto;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.email.event.DeleteBookingEvent;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.geolocation.infrastructure.GeoLocationRepository;
import com.ridemates.app.google.domain.GoogleDirectionData;
import com.ridemates.app.google.domain.GoogleService;
import com.ridemates.app.passenger.domain.Passenger;
import com.ridemates.app.passenger.infrastructure.PassengerRepository;
import com.ridemates.app.price.PriceFormula;
import com.ridemates.app.route.domain.Route;
import com.ridemates.app.route.dto.RouteResponseDto;
import com.ridemates.app.route.infrastructure.RouteRepository;
import com.ridemates.app.travel.domain.Travel;
import com.ridemates.app.travel.infrastructure.TravelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookingService extends GenericService<Booking, BookingResponseDto, Long, BookingRepository> implements ApplicationEventPublisherAware {

    @Autowired
    GeoLocationRepository geoLocationRepository;
    private final RouteRepository routeRepository;
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final DriverRepository driverRepository;
    private final TravelRepository travelRepository;
    private final GoogleService googleService;
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(
            ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public BookingService(BookingRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper, RouteRepository routeRepository, PassengerRepository passengerRepository, BookingRepository bookingRepository, DriverRepository driverRepository, TravelRepository travelRepository, GeoLocationRepository geoLocationRepository, PriceFormula priceFormula, GoogleService googleService) {
        super("Booking",
                repository,
                objectMapper,
                modelMapper,
                Booking.class,
                BookingResponseDto.class);
        this.routeRepository = routeRepository;
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.driverRepository = driverRepository;
        this.travelRepository = travelRepository;
        this.googleService = googleService;
    }

    public BookingResponseDto book(BookingRequestDto bookingRequestDto) {

        Route route = routeRepository.findById(bookingRequestDto.getRouteId())
                .orElseThrow(()-> new NotFoundException("Route", bookingRequestDto.getRouteId(), "id"));

        Passenger passenger = passengerRepository.findById(bookingRequestDto.getPassengerId())
                .orElseThrow(()-> new NotFoundException("Passenger", bookingRequestDto.getPassengerId(), "id"));

        if (route.getCapacity()>0) {
            route.setCapacity(route.getCapacity()-1);
        } else {
            throw new AccessDeniedException("Ruta llena");
        }

        var passengerEmail = passenger.getEmail();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email.equals(passengerEmail)) {
            throw new AccessDeniedException("No se puede reservar un viaje para uno mismo");
        }

        Booking booking = new Booking(passenger,
                route,
                bookingRequestDto.getPrice());

        booking = bookingRepository.save(booking);
        passenger.setBooking(booking);
        return toResponseDto(booking);
    }

    public void delete(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva", id, "id"));

        booking.getRoute().setCapacity(booking.getRoute().getCapacity()+1);
        publisher.publishEvent(new DeleteBookingEvent(booking.getPassenger(), booking));
        bookingRepository.deleteById(id);

    }

    public BookingResponseDto setStatus(Long id, BookingStatusDto dto) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking", id, "id"));

        booking.setStatus(dto.getStatus());

        if (booking.getStatus() == Booking.Status.CANCELLED) {
            booking.getRoute().setCapacity(booking.getRoute().getCapacity() + 1);
        } else if (booking.getStatus() == Booking.Status.ACCEPTED) {
            Travel travel = createTravelForBooking(booking);
            travel = travelRepository.save(travel);
            booking.setTravel(travel);
        }
        //publisher.publishEvent(new StatusBookingEvent(booking));
        booking = bookingRepository.save(booking);
        return toResponseDto(booking);
    }

    private Travel createTravelForBooking(Booking booking) {
        Travel travel = new Travel();
        travel.setBooking(booking);
        travel.setCreatedAt(LocalDate.now());
        travel.setDestination(booking.getRoute().getDestination());
        travel.setTravelStatus(Travel.TravelStatus.WAITING);
        //travel.setDistance(booking.getRoute().getDistance());
        return travel;
    }

    public Page<BookingResponseDto> getBookingsByEmail(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Passenger passenger = passengerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Passenger", email, "email"));

        List<BookingResponseDto> bookingResponseDtos = new ArrayList<>();
        List<Booking> bookings = passenger.getBookings();

        for (Booking booking : bookings) {
            BookingResponseDto bookingResponseDto = toResponseDto(booking);
            bookingResponseDtos.add(bookingResponseDto);
        }

        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return new PageImpl<>(bookingResponseDtos, pageable, bookingResponseDtos.size());
    }

    public boolean canSetStatus(Long id, String email) {

        driverRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Driver", email, "email"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Booking", id, "id"));

        return Objects.equals(booking.getRoute().getDriver().getEmail(), email);
    }

    public BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setDriver(booking.getRoute().getDriver().getFirstName() + " " + booking.getRoute().getDriver().getLastName());
        bookingResponseDto.setPassenger(booking.getPassenger().getFirstName() + " " + booking.getPassenger().getLastName());
        bookingResponseDto.setDateTime(booking.getRoute().getDateTime());
        bookingResponseDto.setStatus(booking.getStatus());
        return bookingResponseDto;
    }

    public BookingIntentDto intent(Long id, BookingCalculateDto bookingRequestDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Booking", id, "id"));

        BookingIntentDto bookingIntentDto = new BookingIntentDto();
        GoogleDirectionData data = googleService.calculatePriceAndTime(
                bookingRequestDto.getOrigin(),
                bookingRequestDto.getDestination());
        bookingIntentDto.setPrice(data.getPrice());
        bookingIntentDto.setTimeEstimated(data.getDurationSeconds());

        bookingIntentDto.setDestination(bookingRequestDto.getDestination());
        bookingIntentDto.setOrigin(bookingRequestDto.getOrigin());

        Route route = booking.getRoute();
        RouteResponseDto response = new RouteResponseDto();
        response.setId(route.getId());
        response.setCapacity(route.getCapacity());
        response.setDateTime(route.getDateTime());
        response.setDestination(route.getDestination());
        response.setOrigin(route.getOrigin());

        bookingIntentDto.setRoute(response);
        bookingIntentDto.setDriverId(route.getDriver().getId());

        return bookingIntentDto;
    }
}