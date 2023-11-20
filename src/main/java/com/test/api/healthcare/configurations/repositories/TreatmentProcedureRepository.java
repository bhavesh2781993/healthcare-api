package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.TreatmentProcedure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreatmentProcedureRepository extends JpaRepository<TreatmentProcedure, Long> {

    List<TreatmentProcedure> findAllByClinicId(Long clinicId);

    Optional<TreatmentProcedure> findByIdAndClinicId(Long treatmentProcedureId, Long clinicId);
}
