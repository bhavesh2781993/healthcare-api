package com.test.api.healthcare.configurations.models.entities;


import com.test.api.healthcare.common.models.entities.Auditable;
import com.test.api.healthcare.configurations.constants.WorkflowType;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Getter
@Setter
@Entity
@Table(name = "workflows",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_workflow_name_type",
            columnNames = {"name", "type"})
    }
)
public class Workflow extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_workflows")
    @SequenceGenerator(name = "seq_workflows", allocationSize = 5)
    @Column(name = "workflow_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private WorkflowType type;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "workflow")
    private List<WorkflowStep> workflowSteps;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "workflow")
    private List<ClinicWorkflow> clinicWorkflows;

    public void addWorkflowSteps() {
        if (!CollectionUtils.isEmpty(getWorkflowSteps())) {
            getWorkflowSteps()
                .forEach(workflowStep -> workflowStep.setWorkflow(this));
        }
    }

}
