package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.Frequency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FrequencyRepository extends JpaRepository<Frequency, Long> {

    List<Frequency> findAllByClinicId(Long clinicId);

}
