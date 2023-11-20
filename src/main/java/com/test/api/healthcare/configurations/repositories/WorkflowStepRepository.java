package com.test.api.healthcare.configurations.repositories;


import com.test.api.healthcare.configurations.models.entities.WorkflowStep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {

    @Query("""
        SELECT wfs FROM WorkflowStep wfs
        WHERE wfs.workflow.id = :workflowId
        AND wfs.seq = :seq
        """)
    Optional<WorkflowStep> findWorkflowStep(Long workflowId, Integer seq);
}
