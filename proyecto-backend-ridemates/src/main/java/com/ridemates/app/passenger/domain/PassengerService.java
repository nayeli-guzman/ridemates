package com.ridemates.app.passenger.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.passenger.dto.PassengerResponseDto;
import com.ridemates.app.passenger.infrastructure.PassengerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService extends GenericService<Passenger, PassengerResponseDto, Long, PassengerRepository> {
    @Autowired
    protected PassengerService(PassengerRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper) {
        super("Passenger",
                repository,
                objectMapper,
                modelMapper,
                Passenger.class,
                PassengerResponseDto.class);
    }
}
