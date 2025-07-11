package com.ridemates.app.passenger.domain;

import com.google.common.base.Objects;
import com.ridemates.app.booking.domain.Booking;
import com.ridemates.app.user.domain.Gender;
import com.ridemates.app.user.domain.Role;
import com.ridemates.app.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Passenger extends User {

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<Booking> bookings;

    public Passenger(
            String firstName,
            String lastName,
            String email,
            String phone,
            Gender gender,
            LocalDate now,
            LocalDate birthDate,
            String password
    ) {
        super(firstName, lastName, email, phone, gender, password, now, birthDate, Role.PASSENGER);
        bookings = new ArrayList<>();
    }

    public void setBooking(Booking booking) {
        bookings.add(booking);
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "bookings=" + bookings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equal(bookings, passenger.bookings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookings);
    }
}
