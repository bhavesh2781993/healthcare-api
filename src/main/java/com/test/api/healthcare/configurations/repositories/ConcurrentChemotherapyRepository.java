package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ConcurrentChemotherapy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcurrentChemotherapyRepository extends JpaRepository<ConcurrentChemotherapy, Long> {

    List<ConcurrentChemotherapy> findAllByClinicId(Long clinicId);

    boolean existsByClinicId(Long clinicId);

}
