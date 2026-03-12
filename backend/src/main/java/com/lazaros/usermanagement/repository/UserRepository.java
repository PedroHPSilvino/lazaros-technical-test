package com.lazaros.usermanagement.repository;

import com.lazaros.usermanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = "profiles")
    List<UserEntity> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "profiles")
    Optional<UserEntity> findWithProfilesById(Long id);
}