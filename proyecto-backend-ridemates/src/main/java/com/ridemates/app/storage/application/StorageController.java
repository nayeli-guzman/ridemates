package com.ridemates.app.storage.application;

import com.ridemates.app.storage.domain.StorageService;
import com.ridemates.app.user.domain.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/profile")
    public ResponseEntity<?> uploadProfile(@RequestBody MultipartFile file, Principal principal) throws Exception {
        storageService.uploadProfile(file, principal);
        return ResponseEntity.ok("Avatar profile updated");
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfilePic(Principal principal) {
        String result = storageService.getProfilePic(principal);
        return result == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(result);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfilePic(Principal principal) {
        boolean result = storageService.deleteProfilePic(principal);
        return !result ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
    }
}
