package com.test.api.healthcare.configurations.repositories;

import com.test.api.healthcare.configurations.models.entities.Workflow;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

}
