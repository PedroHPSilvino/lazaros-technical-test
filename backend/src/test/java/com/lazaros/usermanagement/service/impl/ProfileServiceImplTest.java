package com.lazaros.usermanagement.service.impl;

import com.lazaros.usermanagement.dto.profile.CreateProfileRequest;
import com.lazaros.usermanagement.dto.profile.ProfileResponse;
import com.lazaros.usermanagement.dto.profile.UpdateProfileRequest;
import com.lazaros.usermanagement.entity.ProfileEntity;
import com.lazaros.usermanagement.exception.ResourceNotFoundException;
import com.lazaros.usermanagement.mapper.ProfileMapper;
import com.lazaros.usermanagement.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void shouldCreateProfileSuccessfully() {
        CreateProfileRequest request = new CreateProfileRequest("Administrator");
        ProfileEntity savedEntity = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();
        ProfileResponse expectedResponse = new ProfileResponse(1L, "Administrator");

        when(profileRepository.save(any(ProfileEntity.class))).thenReturn(savedEntity);
        when(profileMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        ProfileResponse response = profileService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Administrator", response.description());
        verify(profileRepository).save(any(ProfileEntity.class));
        verify(profileMapper).toResponse(savedEntity);
    }

    @Test
    void shouldReturnAllProfiles() {
        ProfileEntity profile = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();
        ProfileResponse response = new ProfileResponse(1L, "Administrator");

        when(profileRepository.findAll()).thenReturn(List.of(profile));
        when(profileMapper.toResponse(profile)).thenReturn(response);

        List<ProfileResponse> result = profileService.findAll();

        assertEquals(1, result.size());
        assertEquals("Administrator", result.getFirst().description());
    }

    @Test
    void shouldReturnProfileById() {
        ProfileEntity profile = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();
        ProfileResponse response = new ProfileResponse(1L, "Administrator");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileMapper.toResponse(profile)).thenReturn(response);

        ProfileResponse result = profileService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("Administrator", result.description());
    }

    @Test
    void shouldThrowWhenProfileNotFoundById() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profileService.findById(99L));
    }

    @Test
    void shouldUpdateProfileSuccessfully() {
        UpdateProfileRequest request = new UpdateProfileRequest("Support Team");
        ProfileEntity profile = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();
        ProfileResponse response = new ProfileResponse(1L, "Support Team");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);
        when(profileMapper.toResponse(profile)).thenReturn(response);

        ProfileResponse result = profileService.update(1L, request);

        assertEquals("Support Team", result.description());
        verify(profileRepository).save(profile);
    }

    @Test
    void shouldDeleteProfileSuccessfully() {
        when(profileRepository.existsById(1L)).thenReturn(true);

        profileService.delete(1L);

        verify(profileRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingProfile() {
        when(profileRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> profileService.delete(1L));
    }
}