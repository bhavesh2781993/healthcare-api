package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.ClinicDepartment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClinicDepartmentRepository extends JpaRepository<ClinicDepartment, Long> {

    Page<ClinicDepartment> findAllByClinicLocationClinicId(Long clinicId, Pageable pageable);
}
