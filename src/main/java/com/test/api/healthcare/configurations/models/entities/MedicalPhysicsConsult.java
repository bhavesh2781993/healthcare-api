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

@Getter
@Setter
@Entity
@Table(name = "medical_physics_consults_lookup")
public class MedicalPhysicsConsult extends Auditable {

    public static final String FK_MEDICAL_PHYSICS_CONSULT_LOOKUP_CLINIC_ID = "fk_medical_physics_consult_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medical_physics_consults_lookup")
    @SequenceGenerator(name = "seq_medical_physics_consults_lookup", allocationSize = 5)
    @Column(name = "medical_physics_consult_lookup_id")
    private Long id;

    private String consult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_MEDICAL_PHYSICS_CONSULT_LOOKUP_CLINIC_ID)
    )
    private Clinic clinic;
}
