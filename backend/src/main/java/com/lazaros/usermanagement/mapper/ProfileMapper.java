package com.lazaros.usermanagement.mapper;

import com.lazaros.usermanagement.dto.profile.ProfileResponse;
import com.lazaros.usermanagement.entity.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponse toResponse(ProfileEntity entity) {
        return new ProfileResponse(
            entity.getId(),
            entity.getDescription()
        );
    }
}