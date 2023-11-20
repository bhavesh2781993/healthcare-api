package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.Instruction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {
    List<Instruction> findAllByClinicId(Long clinicId);

    Optional<Instruction> findByIdAndClinicId(Long instructionsId, Long clinicId);

    boolean existsById(Long instructionsId);

}
