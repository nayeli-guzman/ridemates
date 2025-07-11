package com.ridemates.app.driver.domain;

import com.ridemates.app.route.domain.Route;
import com.ridemates.app.user.domain.Gender;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import com.ridemates.app.user.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "drivers")
public class Driver extends User {

    @Column(unique = true, nullable = false)
    private String license;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column
    private Integer travelCount;

    @Column(nullable = false)
    private Double wallet;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routes;

    public Driver(
            String firstName,
            String lastName,
            String email,
            String phone,
            Gender gender,
            String password,
            String license,
            LocalDate createdAt,
            LocalDate birthDate,
            Vehicle vehicle
    ) {
        super(firstName, lastName, email, phone, gender, password, createdAt, birthDate, Role.DRIVER);
        this.license = license;
        this.vehicle = vehicle;
        this.wallet = 0.0;
        this.travelCount = 0;
        this.routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        routes.add(route);
        route.setDriver(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Driver driver = (Driver) o;

        if (!license.equals(driver.license)) return false;
        if (!vehicle.equals(driver.vehicle)) return false;
        if (!travelCount.equals(driver.travelCount)) return false;
        if (!wallet.equals(driver.wallet)) return false;
        return routes.equals(driver.routes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(license, vehicle.getId(), travelCount, wallet, routes);
    }
}
