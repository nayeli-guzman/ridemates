package com.ridemates.app.payment.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.general.domain.Created;
import com.ridemates.app.general.exception.ConflictException;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.passenger.infrastructure.PassengerRepository;
import com.ridemates.app.payment.dto.PaymentRequestDto;
import com.ridemates.app.payment.dto.PaymentResponseDto;
import com.ridemates.app.payment.infraestructure.PaymentRepository;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.travel.domain.Travel;
import com.ridemates.app.travel.infrastructure.TravelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService extends GenericService<Payment, PaymentResponseDto, Long, PaymentRepository> {
    private final TravelRepository travelRepository;
    private final PassengerRepository passengerRepository;

    @Autowired
    public PaymentService(PaymentRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper, TravelRepository travelRepository,
                          PassengerRepository passengerRepository) {
        super("Payment",
                repository,
                objectMapper,
                modelMapper,
                Payment.class,
                PaymentResponseDto.class);
        this.travelRepository = travelRepository;
        this.passengerRepository = passengerRepository;
    }

    public Page<PaymentResponseDto> getPaymentsByUserId(Long id, int page, int size) {
        return repository.findByTravel_Booking_Passenger_Id(id, Pageable.ofSize(size).withPage(page))
                .map(payment -> modelMapper.map(payment, PaymentResponseDto.class));
    }

    public Page<PaymentResponseDto> getMyPayments(int page, int size) {
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByTravel_Booking_Passenger_Email(mail, Pageable.ofSize(size).withPage(page))
                .map(payment -> modelMapper.map(payment, PaymentResponseDto.class));
    }

    public PaymentResponseDto getPaymentByTravelId(Long travelId) {
        Payment payment = repository.findByTravel_Id(travelId)
                .orElseThrow(() -> new NotFoundException("Payment", travelId, "travelId"));
        return modelMapper.map(payment, PaymentResponseDto.class);
    }

    public Created<PaymentResponseDto, Long> createPayment(Long id, PaymentRequestDto dto, String intent) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Travel", id, "id"));
        if (travel.getPayment() != null) {
            throw new ConflictException("Payment", String.valueOf(id), "Travel");
        }

        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setTravel(travel);
        payment.setPaymentIntentId(intent);
        payment.setCreatedAt(ZonedDateTime.now());
        save(payment);

        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setMethod(payment.getMethod());
        responseDto.setTravelId(payment.getTravel().getId());
        responseDto.setAmount(payment.getAmount());
        responseDto.setPaymentIntentId(payment.getPaymentIntentId());

        return Created.of(responseDto, payment.getId());
    }
}