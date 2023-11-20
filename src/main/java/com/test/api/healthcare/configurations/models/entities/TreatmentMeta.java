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
@Table(name = "treatment_meta_lookup")
@Entity
public class TreatmentMeta extends Auditable {

    public static final String FK_TREATMENT_META_LOOKUP_CLINIC_ID = "fk_treatment_meta_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_treatment_meta_lookup")
    @SequenceGenerator(name = "seq_treatment_meta_lookup", allocationSize = 5)
    @Column(name = "treatment_meta_lookup_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_TREATMENT_META_LOOKUP_CLINIC_ID)
    )
    private Clinic clinic;

    @Column(name = "meta_type")
    private String metaType;

    @Column(name = "meta_value")
    private String metaValue;

}
