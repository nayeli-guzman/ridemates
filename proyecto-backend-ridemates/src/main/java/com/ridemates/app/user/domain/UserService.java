package com.ridemates.app.user.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.ridemates.app.email.domain.EmailService;
import com.ridemates.app.general.domain.GenericService;
import com.ridemates.app.general.exception.NotFoundException;
import com.ridemates.app.user.dto.UserPatchDto;
import com.ridemates.app.user.dto.UserResponseDto;
import com.ridemates.app.user.infrastructure.UserRepository;
import com.ridemates.app.utils.MethodUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends GenericService<User, UserResponseDto, Long, UserRepository> {

    @Autowired
    public UserService(UserRepository userRepository, ObjectMapper objectMapper, ModelMapper modelMapper) {
        super("User",
                userRepository,
                objectMapper,
                modelMapper,
                User.class,
                UserResponseDto.class);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", email, "mail"));
    }

    public User findMeUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByEmail(name)
                .orElseThrow(() -> new NotFoundException("User", name, "mail"));
    }

    public UserResponseDto findMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = repository.findByEmail(name)
                .orElseThrow(() -> new NotFoundException("User", name, "mail"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    public User updateMe(UserPatchDto patch) {
        User patched = findMeUser();

        if (patch.getFirstName() != null) {
            patched.setFirstName(patch.getFirstName());
        }
        if (patch.getLastName() != null) {
            patched.setLastName(patch.getLastName());
        }
        if (patch.getPhone() != null) {
            patched.setPhone(patch.getPhone());
        }
        if (patch.getGender() != null) {
            patched.setGender(patch.getGender());
        }
        if (patch.getCalification() != null) {
            patched.setCalification(patch.getCalification());
        }
        if (patch.getBirthDate() != null) {
            patched.setBirthDate(patch.getBirthDate());
        }

        save(patched);
        return patched;
    }

}


