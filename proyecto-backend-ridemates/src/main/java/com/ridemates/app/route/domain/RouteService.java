package com.ridemates.app.route.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.domain.DriverService;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.general.domain.Created;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.geolocation.infrastructure.GeoLocationRepository;
import com.ridemates.app.route.dto.RouteRequestDto;
import com.ridemates.app.route.dto.RouteResponseDto;
import com.ridemates.app.route.infrastructure.RouteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService extends GenericService<Route, RouteResponseDto, Long, RouteRepository> {

    @Autowired
    private DriverService driverService;

    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private GeoLocationRepository geoLocationRepository;
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    public RouteService(RouteRepository repository, ObjectMapper objectMapper, ModelMapper mapper, DriverService driverService) {
        super("Route",
                repository,
                objectMapper,
                mapper,
                Route.class,
                RouteResponseDto.class);
        this.driverService = driverService;
    }

    public Created<RouteResponseDto, Long> createRoute(RouteRequestDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Driver", email, "email"));
        /*GeoLocationDto originDto = dto.getOriginDto();
        GeoLocation origin = new GeoLocation(
                originDto.getLongitude(),
                originDto.getLatitude()
        );


        GeoLocationDto destinationDto = dto.getDestinationDto();
        GeoLocation destination = new GeoLocation(
                destinationDto.getLongitude(),
                destinationDto.getLatitude()
        );

        geoLocationRepository.save(origin);
        geoLocationRepository.save(destination);*/
        Route route = new Route(
                dto.getOrigin(),
                dto.getDestination(),
                dto.getCapacity(),
                driver,
                dto.getDateTime()
        );

        route = routeRepository.save(route);
        driver.addRoute(route);
        driverRepository.save(driver);

        return Created.of(
                getRouteResponseDto(route),
                route.getId()
        );
    }

    public RouteResponseDto findRoute(Long id) {
        return getRouteResponseDto(routeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Route", id, "id")));
    }

    public Page<RouteResponseDto> getAllRoutes(int page, int size) {
        return routeRepository.findAll(PageRequest.of(page, size))
                .map(this::getRouteResponseDto);
    }

    private RouteResponseDto getRouteResponseDto(Route route) {
        RouteResponseDto dto = new RouteResponseDto();
        dto.setId(route.getId());
        dto.setDriverFullName(route.getDriver().getFirstName() + " " +route.getDriver().getLastName());
        dto.setCapacity(route.getCapacity());
        dto.setOrigin(route.getOrigin());
        dto.setDateTime(route.getDateTime());
        dto.setDestination(route.getDestination());
        return dto;
    }

    public List<RouteResponseDto> findAllRoutesOfDriver(Long driverId) {

        List<Route> routes = driverService.findById(driverId).getRoutes();
        List<RouteResponseDto> routeResponseDtos = new ArrayList<>();

        for (Route route : routes) {
            routeResponseDtos.add(getRouteResponseDto(route));
        }

        return routeResponseDtos;
    }

    public List<RouteResponseDto> findAllRoutesOfMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return driverService.findByEmail(name)
                .getRoutes()
                .stream()
                .map(route -> {
                    return getRouteResponseDto(route);
                })
                .toList();
    }
}
