package com.test.api.healthcare.configurations.repositories;


import com.test.api.healthcare.configurations.models.entities.TreatmentMeta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreatmentMetaRepository extends JpaRepository<TreatmentMeta, Long> {

    List<TreatmentMeta> findAllByMetaTypeAndClinicId(String metaType, Long clinicId);

    List<TreatmentMeta> findAllByClinicId(Long clinicId);

    Optional<TreatmentMeta> findByIdAndClinicId(Long treatmentMetaId, Long clinicId);

    boolean existsByIdAndMetaType(Long treatmentMetaId, String treatmentMetaType);

}
