package com.ridemates.app.route;

import com.ridemates.app.AbstractContainerBaseTest;
import com.ridemates.app.driver.domain.Driver;
import com.ridemates.app.driver.infrastructure.DriverRepository;
import com.ridemates.app.geolocation.infrastructure.GeoLocationRepository;
import com.ridemates.app.ors.domain.GeoLocation;
import com.ridemates.app.route.domain.Route;
import com.ridemates.app.route.infrastructure.RouteRepository;
import com.ridemates.app.user.domain.Gender;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.vehicle.domain.Vehicle;
import com.ridemates.app.vehicle.infrastructure.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Testcontainers

public class RouteTestContainersTest extends AbstractContainerBaseTest {

    @Autowired
    DriverRepository driverRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    GeoLocationRepository geoLocationRepository;

    Driver driver;
    Vehicle vehicle;
    Route route;

    String token = "";
    Long driverId;

    @BeforeEach
    public void setUp() throws Exception {

        vehicleRepository.deleteAll();
        driverRepository.deleteAll();

        vehicle = new Vehicle(
                "aaa-aa-a",
                4,
                "012918",
                "XXXXX"
        );

        driver = new Driver(
                "Adrian",
                "Montes",
                "adrian.montes@utec.edu.pe",
                "123456789",
                Gender.M,
                "passsssssword",
                "yyyyyyy",
                LocalDate.now(),
                LocalDate.now(),
                vehicle
        );
        driver.setCalification(0);
        driver.setRole(Role.DRIVER);
        driver.setVerificationCode(null);
        driver.setVerificationCodeExpiresAt(null);
        driver.setEnabled(true);
        vehicle.setDriver(driver);

        vehicle = vehicleRepository.save(vehicle);
        driver = driverRepository.save(driver);
    }

    @Test
    public void createRoute() throws Exception {
        GeoLocation origin = new GeoLocation(120000., 13000.);
        GeoLocation destination = new GeoLocation(120000., 13000.);

        origin = geoLocationRepository.save(origin);
        destination = geoLocationRepository.save(destination);

        route = new Route(
                origin.toString(),
                destination.toString(),
                4,
                driver,
                ZonedDateTime.now()
        );

        route = routeRepository.save(route);
        driver.addRoute(route);

        Assertions.assertNotNull(route.getId());
        Assertions.assertEquals(route.getDriver().getEmail(), "adrian.montes@utec.edu.pe");
        Assertions.assertEquals(route.getDriver().getLicense(), "yyyyyyy");
        Assertions.assertEquals(driver.getRoutes().size(),1);

    }

    @Test
    public void findRouteByIdPassenger() throws Exception {

        GeoLocation origin = new GeoLocation(120000., 13000.);
        GeoLocation destination = new GeoLocation(120000., 13000.);

        origin = geoLocationRepository.save(origin);
        destination = geoLocationRepository.save(destination);

        route = new Route(
                origin.toString(),
                destination.toString(),
                4,
                driver,
                ZonedDateTime.now()
        );

        route = routeRepository.save(route);
        driver.addRoute(route);

        Route route2 = new Route(
                origin.toString(),
                destination.toString(),
                4,
                driver,
                ZonedDateTime.now().plusMinutes(60)
        );

        route2 = routeRepository.save(route2);
        driver.addRoute(route2);

        var res = routeRepository.findAllByDriverId(driver.getId());
        Assertions.assertEquals(res.get().size(), 2);
    }

}
