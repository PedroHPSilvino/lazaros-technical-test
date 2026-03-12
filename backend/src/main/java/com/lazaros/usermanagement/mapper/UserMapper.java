package com.lazaros.usermanagement.mapper;

import com.lazaros.usermanagement.dto.user.UserResponse;
import com.lazaros.usermanagement.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ProfileMapper profileMapper;

    public UserResponse toResponse(UserEntity entity) {
        return new UserResponse(
            entity.getId(),
            entity.getName(),
            entity.getProfiles()
                .stream()
                .map(profileMapper::toResponse)
                .toList()
        );
    }
}