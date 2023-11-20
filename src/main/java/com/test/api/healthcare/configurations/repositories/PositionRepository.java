package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.Position;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findAllByPositionTypeAndClinicId(String positionType, Long clinicId);

    List<Position> findAllByClinicId(Long clinicId);

    Optional<Position> findByIdAndClinicId(Long positionId, Long clinicId);
}
