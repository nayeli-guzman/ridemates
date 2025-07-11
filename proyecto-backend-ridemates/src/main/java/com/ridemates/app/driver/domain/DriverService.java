package com.ridemates.app.driver.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.driver.dto.DriverResponseDto;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.vehicle.domain.Vehicle;
import com.ridemates.app.vehicle.domain.VehicleService;
import com.ridemates.app.vehicle.dto.VehicleRequestDto;
import com.ridemates.app.vehicle.dto.VehicleResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DriverService extends GenericService<Driver, DriverResponseDto, Long, DriverRepository> {
    private final VehicleService vehicleService;

    @Autowired
    public DriverService(DriverRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper, VehicleService vehicleService) {
        super("Driver",
                repository,
                objectMapper,
                modelMapper,
                Driver.class,
                DriverResponseDto.class);
        this.vehicleService = vehicleService;
    }

    public Driver findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(()-> new NotFoundException("Driver", email, "email"));
    }


    public VehicleResponseDto changeMyVehicle(VehicleRequestDto newVehicle) {
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        Driver driver = findByEmail(mail);

        Vehicle vehicle = new Vehicle(
                newVehicle.getPlate(),
                newVehicle.getCapacity(),
                newVehicle.getSoat(),
                newVehicle.getModel()
        );
        vehicle.setCreatedAt(LocalDate.now());

        Vehicle oldVehicle = driver.getVehicle();
        oldVehicle.setDriver(null);
        vehicleService.delete(oldVehicle);

        driver.setVehicle(vehicle);
        save(driver);

        vehicle.setDriver(driver);
        vehicleService.save(vehicle);

        return modelMapper.map(vehicle, VehicleResponseDto.class);
    }

    public DriverResponseDto findMe() {
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        return modelMapper.map(findByEmail(mail), DriverResponseDto.class);
    }
}
