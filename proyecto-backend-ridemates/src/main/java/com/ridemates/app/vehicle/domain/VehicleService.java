package com.ridemates.app.vehicle.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.domain.DriverService;
import com.ridemates.app.driver.dto.DriverResponseDto;
import com.ridemates.app.general.domain.Created;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.vehicle.dto.VehicleRequestDto;
import com.ridemates.app.vehicle.dto.VehicleResponseDto;
import com.ridemates.app.vehicle.infrastructure.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VehicleService extends GenericService<Vehicle, VehicleResponseDto, Long, VehicleRepository> {

    @Autowired
    public VehicleService(VehicleRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper) {
        super("Vehicle",
                repository,
                objectMapper,
                modelMapper,
                Vehicle.class,
                VehicleResponseDto.class);
    }

    public DriverResponseDto findDriverOf(Long id) {
        return modelMapper.map(findById(id).getDriver(), DriverResponseDto.class);
    }
}
