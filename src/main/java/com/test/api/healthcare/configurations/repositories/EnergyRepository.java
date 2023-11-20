package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.Energy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnergyRepository extends JpaRepository<Energy, Long> {

    List<Energy> findAllByClinicId(Long clinicId);

}
