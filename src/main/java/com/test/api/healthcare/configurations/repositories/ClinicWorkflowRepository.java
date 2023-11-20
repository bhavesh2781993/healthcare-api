package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.constants.WorkflowType;
import com.test.api.healthcare.configurations.models.entities.ClinicWorkflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClinicWorkflowRepository extends JpaRepository<ClinicWorkflow, Long> {

    @Query("""
        SELECT cwf FROM ClinicWorkflow cwf
        LEFT JOIN FETCH cwf.workflow w
        LEFT JOIN FETCH w.workflowSteps ws
        WHERE w.type = :workflowType
        AND ws.seq = :workflowStepSeq
        AND cwf.clinic.id = :clinicId
        """)
    Optional<ClinicWorkflow> findClinicWorkflow(WorkflowType workflowType, int workflowStepSeq, long clinicId);

}
