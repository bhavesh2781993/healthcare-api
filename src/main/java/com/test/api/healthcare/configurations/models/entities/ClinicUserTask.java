package com.test.api.healthcare.configurations.models.entities;

import com.test.api.healthcare.common.models.entities.Auditable;
import com.test.api.healthcare.configurations.constants.UserTaskStatus;
import com.test.api.healthcare.configurations.constants.UserTaskType;
import com.test.api.healthcare.patients.models.entities.Patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Getter
@Setter
@Entity
@Table(name = "clinic_user_tasks")
public class ClinicUserTask extends Auditable {

    public static final String FK_CLINIC_USER_TASK_PATIENT_ID = "fk_clinic_user_task_patient_id";
    public static final String FK_CLINIC_USER_TASK_CLINIC_ID = "fk_clinic_user_task_clinic_id";
    public static final String FK_CLINIC_USER_TASK_TASK_ASSIGNED_TO_ID = "fk_clinic_user_task_task_assigned_to_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clinic_user_tasks")
    @SequenceGenerator(name = "seq_clinic_user_tasks", allocationSize = 5)
    @Column(name = "clinic_user_task_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private UserTaskType taskType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "patient_id",
        referencedColumnName = "patient_id",
        nullable = false,
        foreignKey = @ForeignKey(name = FK_CLINIC_USER_TASK_PATIENT_ID)
    )
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        nullable = false,
        foreignKey = @ForeignKey(name = FK_CLINIC_USER_TASK_CLINIC_ID)
    )
    private Clinic clinic;

    @Column(name = "task_due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime taskDueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "task_assigned_to",
        referencedColumnName = "clinic_user_id",
        foreignKey = @ForeignKey(name = FK_CLINIC_USER_TASK_TASK_ASSIGNED_TO_ID)
    )
    private ClinicUser taskAssignedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private UserTaskStatus taskStatus;

    @Column(name = "task_note")
    private String taskNote;

    @Column(name = "task_ref_id")
    private Long taskRefId;

}
