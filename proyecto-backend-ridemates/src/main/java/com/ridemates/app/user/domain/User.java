package com.ridemates.app.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column()
    private String password;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false)
    private Integer calification;
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column(nullable = false)
    private LocalDate birthDate;
    @Column()
    private String profileKey;

    // security - atributes

    @Column(name="verification_code")
    private String verificationCode;
    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;

    @Column
    private String avatarURL;

    @Column
    private String googleClientUserId;
    @Column
    private String stripeClientUserId;

    private boolean enabled;


    public User(String firstName,
                String lastName,
                String email,
                String phone,
                Gender gender,
                String password,
                LocalDate createdAt,
                LocalDate birthDate,
                Role role
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.calification = 0;
        this.createdAt = createdAt;
        this.birthDate = birthDate;
        this.role = role;
    }

    // override - user details

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(role.asAuthority());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return enabled;
    }


}
