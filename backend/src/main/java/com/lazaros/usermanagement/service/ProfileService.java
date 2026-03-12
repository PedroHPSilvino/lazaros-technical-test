package com.lazaros.usermanagement.service;

import com.lazaros.usermanagement.dto.profile.CreateProfileRequest;
import com.lazaros.usermanagement.dto.profile.ProfileResponse;
import com.lazaros.usermanagement.dto.profile.UpdateProfileRequest;

import java.util.List;

public interface ProfileService {

    ProfileResponse create(CreateProfileRequest request);

    List<ProfileResponse> findAll();

    ProfileResponse findById(Long id);

    ProfileResponse update(Long id, UpdateProfileRequest request);

    void delete(Long id);
}