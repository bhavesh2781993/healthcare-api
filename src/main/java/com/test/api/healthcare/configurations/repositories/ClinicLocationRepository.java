package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ClinicLocation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicLocationRepository extends JpaRepository<ClinicLocation, Long> {

    Page<ClinicLocation> findAllByClinicId(Long clinicId, Pageable pageable);

}
