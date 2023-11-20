package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ImmobilizationDevice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImmobilizationDeviceRepository extends JpaRepository<ImmobilizationDevice, Long> {

    List<ImmobilizationDevice> findAllByClinicId(Long clinicId);

    Optional<ImmobilizationDevice> findByIdAndClinicId(Long immobilizationDeviceId, Long clinicId);

    boolean existsById(Long immobilizationDeviceId);
}
