package com.ridemates.app.vehicle.domain;

import com.ridemates.app.driver.domain.Driver;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String plate;
    @Column(nullable = false)
    private Integer capacity;
    @Column(nullable = false)
    private String soat;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private LocalDate createdAt;

    @OneToOne
    private Driver driver;

    public Vehicle(
            String plate,
            Integer capacity,
            String soat,
            String model
    ) {
        this.plate = plate;
        this.capacity = capacity;
        this.soat = soat;
        this.model = model;
        this.createdAt = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;
        return Objects.equals(id, vehicle.id) && Objects.equals(plate, vehicle.plate) && Objects.equals(capacity, vehicle.capacity) && Objects.equals(soat, vehicle.soat) && Objects.equals(model, vehicle.model) && Objects.equals(createdAt, vehicle.createdAt) && Objects.equals(driver, vehicle.driver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plate, capacity, soat, model, createdAt, driver.getId());
    }
}
