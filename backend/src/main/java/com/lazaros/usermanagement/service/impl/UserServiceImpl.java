package com.lazaros.usermanagement.service.impl;

import com.lazaros.usermanagement.dto.user.CreateUserRequest;
import com.lazaros.usermanagement.dto.user.UpdateUserRequest;
import com.lazaros.usermanagement.dto.user.UserResponse;
import com.lazaros.usermanagement.entity.ProfileEntity;
import com.lazaros.usermanagement.entity.UserEntity;
import com.lazaros.usermanagement.exception.ResourceNotFoundException;
import com.lazaros.usermanagement.mapper.UserMapper;
import com.lazaros.usermanagement.repository.ProfileRepository;
import com.lazaros.usermanagement.repository.UserRepository;
import com.lazaros.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse create(CreateUserRequest request) {
        Set<ProfileEntity> profiles = getProfilesByIds(request.profileIds());

        UserEntity user = UserEntity.builder()
            .name(request.name())
            .profiles(profiles)
            .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAllBy()
            .stream()
            .map(userMapper::toResponse)
            .toList();
    }

    @Override
    public UserResponse findById(Long id) {
        UserEntity user = userRepository.findWithProfilesById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        UserEntity user = userRepository.findWithProfilesById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        Set<ProfileEntity> profiles = getProfilesByIds(request.profileIds());

        user.setName(request.name());
        user.setProfiles(profiles);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    private Set<ProfileEntity> getProfilesByIds(Set<Long> profileIds) {
        List<ProfileEntity> foundProfiles = profileRepository.findAllById(profileIds);

        if (foundProfiles.size() != profileIds.size()) {
            throw new ResourceNotFoundException("One or more profiles were not found");
        }

        return new HashSet<>(foundProfiles);
    }
}