package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalPhysicsConsultRepository extends JpaRepository<MedicalPhysicsConsult, Long> {

    List<MedicalPhysicsConsult> findByClinicId(Long clinicId);

    Optional<MedicalPhysicsConsult> findByIdAndClinicId(Long medicalPhysicsConsultId, Long clinicId);
}


