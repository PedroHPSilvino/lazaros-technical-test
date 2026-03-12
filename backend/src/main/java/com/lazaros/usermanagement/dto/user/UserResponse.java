package com.lazaros.usermanagement.dto.user;

import com.lazaros.usermanagement.dto.profile.ProfileResponse;

import java.util.List;

public record UserResponse(
    Long id,
    String name,
    List<ProfileResponse> profiles
) {
}