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
@Entity
@Table(name = "imaging_alignments_lookup")
public class ImagingAlignment extends Auditable {

    public static final String FK_IMAGING_ALIGNMENT_LOOKUP_CLINIC_ID = "fk_imaging_alignment_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_imaging_info_lookup")
    @SequenceGenerator(name = "seq_imaging_info_lookup", allocationSize = 5)
    @Column(name = "imaging_alignment_lookup_id")
    private Long id;

    @Column(name = "align_to")
    private String alignTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_IMAGING_ALIGNMENT_LOOKUP_CLINIC_ID)
    )
    private Clinic clinic;
}
