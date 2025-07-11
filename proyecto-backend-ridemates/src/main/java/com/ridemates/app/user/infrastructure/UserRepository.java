package com.ridemates.app.user.infrastructure;

import com.ridemates.app.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleClientUserId(String userId);
    Optional<User> findByVerificationCode(String verificationCode);
}
