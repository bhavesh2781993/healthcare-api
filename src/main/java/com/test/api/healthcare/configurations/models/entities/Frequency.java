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
@Table(name = "frequencies_lookup")
public class Frequency extends Auditable {

    public static final String FK_FREQUENCY_LOOKUP_CLINIC_ID = "fk_frequency_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_frequencies_lookup")
    @SequenceGenerator(name = "seq_frequencies_lookup", allocationSize = 5)
    @Column(name = "frequency_id")
    private Long id;

    private String frequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_FREQUENCY_LOOKUP_CLINIC_ID))
    private Clinic clinic;

}
