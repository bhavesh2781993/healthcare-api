package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.Clinic;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

}
