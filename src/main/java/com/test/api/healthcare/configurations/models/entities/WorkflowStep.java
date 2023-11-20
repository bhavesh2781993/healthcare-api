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
@Table(
    name = "workflow_steps",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_workflow_step_workflow_step",
            columnNames = {"workflow_id", "step"}),
        @UniqueConstraint(
            name = "uk_workflow_step_workflow_seq",
            columnNames = {"workflow_id", "seq"})
    }
)
public class WorkflowStep extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_workflow_steps")
    @SequenceGenerator(name = "seq_workflow_steps", allocationSize = 5)
    @Column(name = "workflow_step_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "workflow_id",
        referencedColumnName = "workflow_id",
        foreignKey = @ForeignKey(name = "fk_workflow_step_workflow_id"))
    private Workflow workflow;

    @Column(name = "step")
    private String step;

    @Column(name = "seq")
    private Integer seq;

}
