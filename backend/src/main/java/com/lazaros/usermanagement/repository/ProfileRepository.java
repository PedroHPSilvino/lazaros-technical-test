package com.lazaros.usermanagement.repository;

import com.lazaros.usermanagement.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    List<ProfileEntity> findAllByOrderByIdAsc();
}