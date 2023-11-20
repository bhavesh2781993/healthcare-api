package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.Fusion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FusionRepository extends JpaRepository<Fusion, Long> {

    List<Fusion> findAllByClinicId(Long clinicId);

    Optional<Fusion> findByIdAndClinicId(Long fusionId, Long clinicId);
}
