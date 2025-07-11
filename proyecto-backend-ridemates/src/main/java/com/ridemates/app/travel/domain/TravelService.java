package com.ridemates.app.travel.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.booking.infrastructure.BookingRepository;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.passenger.dto.PassengerResponseDto;
import com.ridemates.app.payment.domain.Payment;
import com.ridemates.app.travel.dto.TravelResponseDto;
import com.ridemates.app.travel.dto.TravelStatusDto;
import com.ridemates.app.travel.infrastructure.TravelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelService extends GenericService<Travel, TravelResponseDto, Long, TravelRepository> {

    private final BookingRepository bookingRepository;

    @Autowired
    public TravelService(TravelRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper, BookingRepository bookingRepository) {
        super("Travel",
                repository,
                objectMapper,
                modelMapper,
                Travel.class,
                TravelResponseDto.class);
        this.bookingRepository = bookingRepository;
    }


    public Page<TravelResponseDto> findAllTravelsOfDriver(Long driverId, int size, int page) {
        return repository.findAllByBooking_Route_Driver_id(driverId, Pageable.ofSize(size).withPage(page))
                .map(entity -> modelMapper.map(entity, TravelResponseDto.class));
    }

    public Page<TravelResponseDto> findAllTravelsOfMe(int page, int size) {
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        return repository.findAllByBooking_Route_Driver_email(name, Pageable.ofSize(size).withPage(page))
                .map(entity -> modelMapper.map(entity, TravelResponseDto.class));
    }

    public Page<PassengerResponseDto> findAllPassengersOfTravel(Long id, int size, int page) {
        return bookingRepository.findAllByTravel_id(id, Pageable.ofSize(size).withPage(page))
                .map(booking -> modelMapper.map(booking.getPassenger(), PassengerResponseDto.class));
    }

    public TravelStatusDto findStatusOfTravel(Long id) {
        return new TravelStatusDto(findById(id).getTravelStatus());
    }

    public TravelResponseDto findId(Long id) {
        Travel travel = findById(id);
        TravelResponseDto response = new TravelResponseDto();
        response.setDestination(travel.getDestination());
        response.setDistance(travel.getDistance());
        response.setCreatedAt(travel.getCreatedAt());
        Payment payment = travel.getPayment();
        if (payment != null) {
            response.setPaymentId(payment.getId());
        }
        Booking booking = travel.getBooking();
        if (booking != null) {
            response.setBookingId(booking.getId());
        }
        response.setTravelStatus(travel.getTravelStatus());
        return response;
    }

    public void updateState(Long id, TravelStatusDto dto) {
        Travel travel = findById(id);
        travel.setTravelStatus(dto.getTravelStatus());
    }
}
