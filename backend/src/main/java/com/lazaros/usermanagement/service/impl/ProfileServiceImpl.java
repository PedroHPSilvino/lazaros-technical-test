package com.lazaros.usermanagement.service.impl;

import com.lazaros.usermanagement.dto.profile.CreateProfileRequest;
import com.lazaros.usermanagement.dto.profile.ProfileResponse;
import com.lazaros.usermanagement.dto.profile.UpdateProfileRequest;
import com.lazaros.usermanagement.entity.ProfileEntity;
import com.lazaros.usermanagement.exception.ResourceNotFoundException;
import com.lazaros.usermanagement.mapper.ProfileMapper;
import com.lazaros.usermanagement.repository.ProfileRepository;
import com.lazaros.usermanagement.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponse create(CreateProfileRequest request) {
        ProfileEntity profile = ProfileEntity.builder()
            .description(request.description())
            .build();

        return profileMapper.toResponse(profileRepository.save(profile));
    }

    @Override
    public List<ProfileResponse> findAll() {
        return profileRepository.findAll()
            .stream()
            .map(profileMapper::toResponse)
            .toList();
    }

    @Override
    public ProfileResponse findById(Long id) {
        ProfileEntity profile = profileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));

        return profileMapper.toResponse(profile);
    }

    @Override
    public ProfileResponse update(Long id, UpdateProfileRequest request) {
        ProfileEntity profile = profileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));

        profile.setDescription(request.description());

        return profileMapper.toResponse(profileRepository.save(profile));
    }

    @Override
    public void delete(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profile not found with id: " + id);
        }

        profileRepository.deleteById(id);
    }
}