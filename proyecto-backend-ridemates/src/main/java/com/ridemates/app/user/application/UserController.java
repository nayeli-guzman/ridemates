package com.ridemates.app.user.application;

import com.ridemates.app.user.domain.User;
import com.ridemates.app.user.domain.UserService;
import com.ridemates.app.user.dto.UserPatchDto;
import com.ridemates.app.user.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> allUsers() {
        return ResponseEntity.ok(userService.findAllResponse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findByIdResponse(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe() {
        return ResponseEntity.ok(userService.findMe());
    }

    @PatchMapping(value = "/me")
    public ResponseEntity<User> post(@RequestBody UserPatchDto dto) {
        User update = userService.updateMe(dto);
        return ResponseEntity.ok(update);
    }
}
