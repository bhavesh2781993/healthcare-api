package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ClinicUserImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicUserImageRepository extends JpaRepository<ClinicUserImage, Long> {
}
