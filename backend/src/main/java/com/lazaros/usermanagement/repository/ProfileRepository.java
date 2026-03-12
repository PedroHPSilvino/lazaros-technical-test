package com.lazaros.usermanagement.repository;

import com.lazaros.usermanagement.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    boolean existsByUsers_Id(long userId);
}