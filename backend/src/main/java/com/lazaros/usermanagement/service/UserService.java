package com.lazaros.usermanagement.service;

import com.lazaros.usermanagement.dto.user.CreateUserRequest;
import com.lazaros.usermanagement.dto.user.UpdateUserRequest;
import com.lazaros.usermanagement.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(CreateUserRequest request);

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse update(Long id, UpdateUserRequest request);

    void delete(Long id);
}