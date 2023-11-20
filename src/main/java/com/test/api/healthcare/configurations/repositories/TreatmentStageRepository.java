package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.TreatmentStage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TreatmentStageRepository extends JpaRepository<TreatmentStage, Long> {

    List<TreatmentStage> findAllByStageTypeAndClinicId(String stageType, Long clinicId);

    List<TreatmentStage> findAllByClinicId(Long clinicId);

    Optional<TreatmentStage> findByIdAndClinicId(Long treatmentStageId, Long clinicId);

}
