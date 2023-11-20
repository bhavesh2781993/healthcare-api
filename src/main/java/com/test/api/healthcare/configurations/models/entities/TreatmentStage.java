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

@Setter
@Getter
@Table(name = "tnm_stages_lookup")
@Entity
public class TreatmentStage extends Auditable {

    public static final String FK_TNM_STAGE_LOOKUP_CLINIC_ID = "fk_tnm_stage_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tnm_stages_lookup")
    @SequenceGenerator(name = "seq_tnm_stages_lookup", allocationSize = 5)
    @Column(name = "tnm_stage_lookup_id")
    private Long id;

    @Column(name = "stage_type")
    private String stageType;

    @Column(name = "spread_type")
    private String spreadType;

    private String stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_TNM_STAGE_LOOKUP_CLINIC_ID)
    )
    private Clinic clinic;
}
