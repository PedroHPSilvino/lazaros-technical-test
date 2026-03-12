package com.lazaros.usermanagement.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProfileRequest(

    @NotBlank(message = "Description is required")
    @Size(min = 5, message = "Description must have at least 5 characters")
    String description

) {
}