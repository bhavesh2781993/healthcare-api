package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ImagingAlignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImagingAlignmentRepository extends JpaRepository<ImagingAlignment, Long> {

    List<ImagingAlignment> findAllByClinicId(Long clinicId);

    Optional<ImagingAlignment> findByIdAndClinicId(Long imagingAlignmentId, Long clinicId);
}
