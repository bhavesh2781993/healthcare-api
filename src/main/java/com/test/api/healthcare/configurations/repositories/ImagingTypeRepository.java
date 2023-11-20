package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ImagingType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImagingTypeRepository extends JpaRepository<ImagingType, Long> {

    List<ImagingType> findAllByClinicId(Long clinicId);

    Optional<ImagingType> findByIdAndClinicId(Long imagingTypeId, Long clinicId);
}
