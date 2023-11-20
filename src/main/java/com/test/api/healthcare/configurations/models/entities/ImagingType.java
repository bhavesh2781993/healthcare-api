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
@Table(name = "imaging_types_lookup")
public class ImagingType extends Auditable {

    public static final String FK_IMAGING_TYPE_LOOKUP_CLINIC_ID = "fk_imaging_type_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_imaging_type_lookup")
    @SequenceGenerator(name = "seq_imaging_type_lookup", allocationSize = 5)
    @Column(name = "imaging_type_lookup_id")
    private Long id;

    @Column(name = "imaging_type")
    private String imagingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_IMAGING_TYPE_LOOKUP_CLINIC_ID)
    )
    private Clinic clinic;
}


