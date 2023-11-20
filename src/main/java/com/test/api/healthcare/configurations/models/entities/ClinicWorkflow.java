package com.test.api.healthcare.configurations.models.entities;

import com.test.api.healthcare.common.models.entities.Auditable;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Getter
@Setter
@Entity
@Table(name = "clinic_workflows",
    uniqueConstraints = {
        @UniqueConstraint(
            name = ClinicWorkflow.UK_CLINIC_WORKFLOW,
            columnNames = {"clinic_id", "workflow_id"})
    })
public class ClinicWorkflow extends Auditable {

    public static final String FK_CLINIC_WORKFLOW_CLINIC_ID = "fk_clinic_workflow_clinic_id";
    public static final String FK_CLINIC_WORKFLOW_WORKFLOW_ID = "fk_clinic_workflow_workflow_id";
    public static final String UK_CLINIC_WORKFLOW = "uk_clinic_workflow_clinic_and_workflow";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clinic_workflows")
    @SequenceGenerator(name = "seq_clinic_workflows", allocationSize = 5)
    @Column(name = "clinic_workflow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = "fk_clinic_workflow_clinic_id"))
    private Clinic clinic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "workflow_id",
        referencedColumnName = "workflow_id",
        foreignKey = @ForeignKey(name = "fk_clinic_workflow_workflow_id"))
    private Workflow workflow;

    @Column(name = "status")
    private String status;

}
