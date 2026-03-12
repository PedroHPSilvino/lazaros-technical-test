package com.lazaros.usermanagement.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateUserRequest(

    @NotBlank(message = "Name is required")
    @Size(min = 10, message = "Name must have at least 10 characters")
    String name,

    @NotEmpty(message = "At least one profile must be informed")
    Set<Long> profileIds

) {
}