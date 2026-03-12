package com.lazaros.usermanagement.service.impl;

import com.lazaros.usermanagement.dto.profile.ProfileResponse;
import com.lazaros.usermanagement.dto.user.CreateUserRequest;
import com.lazaros.usermanagement.dto.user.UpdateUserRequest;
import com.lazaros.usermanagement.dto.user.UserResponse;
import com.lazaros.usermanagement.entity.ProfileEntity;
import com.lazaros.usermanagement.entity.UserEntity;
import com.lazaros.usermanagement.exception.ResourceNotFoundException;
import com.lazaros.usermanagement.mapper.UserMapper;
import com.lazaros.usermanagement.repository.ProfileRepository;
import com.lazaros.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {
        ProfileEntity profile = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();

        CreateUserRequest request = new CreateUserRequest("Pedro Henrique", Set.of(1L));

        UserEntity savedUser = UserEntity.builder()
            .id(1L)
            .name("Pedro Henrique")
            .profiles(Set.of(profile))
            .build();

        UserResponse expectedResponse = new UserResponse(
            1L,
            "Pedro Henrique",
            List.of(new ProfileResponse(1L, "Administrator"))
        );

        when(profileRepository.findAllById(Set.of(1L))).thenReturn(List.of(profile));
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        UserResponse result = userService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Pedro Henrique", result.name());
        assertEquals(1, result.profiles().size());
    }

    @Test
    void shouldThrowWhenAnyProfileDoesNotExistDuringCreate() {
        CreateUserRequest request = new CreateUserRequest("Pedro Henrique", Set.of(1L, 2L));

        when(profileRepository.findAllById(Set.of(1L, 2L))).thenReturn(List.of(
            ProfileEntity.builder().id(1L).description("Administrator").build()
        ));

        assertThrows(ResourceNotFoundException.class, () -> userService.create(request));
    }

    @Test
    void shouldReturnAllUsers() {
        ProfileEntity profile = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();

        UserEntity user = UserEntity.builder()
            .id(1L)
            .name("Pedro Henrique")
            .profiles(Set.of(profile))
            .build();

        UserResponse response = new UserResponse(
            1L,
            "Pedro Henrique",
            List.of(new ProfileResponse(1L, "Administrator"))
        );

        when(userRepository.findAllByOrderByIdAsc()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        List<UserResponse> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("Pedro Henrique", result.getFirst().name());
    }

    @Test
    void shouldReturnUserById() {
        ProfileEntity profile = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();

        UserEntity user = UserEntity.builder()
            .id(1L)
            .name("Pedro Henrique")
            .profiles(Set.of(profile))
            .build();

        UserResponse response = new UserResponse(
            1L,
            "Pedro Henrique",
            List.of(new ProfileResponse(1L, "Administrator"))
        );

        when(userRepository.findWithProfilesById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.findById(1L);

        assertEquals(1L, result.id());
        assertEquals("Pedro Henrique", result.name());
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        when(userRepository.findWithProfilesById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        ProfileEntity currentProfile = ProfileEntity.builder()
            .id(1L)
            .description("Administrator")
            .build();

        ProfileEntity newProfile = ProfileEntity.builder()
            .id(2L)
            .description("Manager")
            .build();

        UserEntity existingUser = UserEntity.builder()
            .id(1L)
            .name("Pedro Henrique")
            .profiles(Set.of(currentProfile))
            .build();

        UpdateUserRequest request = new UpdateUserRequest("Pedro Henrique Silva", Set.of(2L));

        UserResponse expectedResponse = new UserResponse(
            1L,
            "Pedro Henrique Silva",
            List.of(new ProfileResponse(2L, "Manager"))
        );

        when(userRepository.findWithProfilesById(1L)).thenReturn(Optional.of(existingUser));
        when(profileRepository.findAllById(Set.of(2L))).thenReturn(List.of(newProfile));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toResponse(existingUser)).thenReturn(expectedResponse);

        UserResponse result = userService.update(1L, request);

        assertEquals("Pedro Henrique Silva", result.name());
        assertEquals(1, result.profiles().size());
        assertEquals("Manager", result.profiles().getFirst().description());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingUser() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(1L));
    }
}