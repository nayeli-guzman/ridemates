package com.ridemates.app.geolocation.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.geolocation.infrastructure.GeoLocationRepository;
import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.ors.dto.GeoLocationDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationService extends GenericService<GeoLocation, GeoLocationDto, Long, GeoLocationRepository> {

    protected GeoLocationService(GeoLocationRepository repository, ObjectMapper objectMapper, ModelMapper modelMapper) {
        super("GeoLocation", repository, objectMapper, modelMapper, GeoLocation.class, GeoLocationDto.class);
    }


}