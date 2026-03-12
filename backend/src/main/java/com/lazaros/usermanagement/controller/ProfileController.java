package com.lazaros.usermanagement.controller;

import com.lazaros.usermanagement.dto.profile.CreateProfileRequest;
import com.lazaros.usermanagement.dto.profile.ProfileResponse;
import com.lazaros.usermanagement.dto.profile.UpdateProfileRequest;
import com.lazaros.usermanagement.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody CreateProfileRequest request) {
        ProfileResponse response = profileService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfileResponse>> findAll() {
        return ResponseEntity.ok(profileService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(profileService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(
        @PathVariable long id,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(profileService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}