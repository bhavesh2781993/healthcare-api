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
@Table(name = "concurrent_chemotherapy_lookup")
public class ConcurrentChemotherapy extends Auditable {

    public static final String FK_CONCURRENT_CHEMOTHERAPY_LOOKUP_CLINIC_ID = "fk_concurrent_chemotherapy_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_concurrent_chemotherapy_lookup")
    @SequenceGenerator(name = "seq_concurrent_chemotherapy_lookup", allocationSize = 5)
    @Column(name = "concurrent_chemotherapy_lookup_id")
    private Long id;

    @Column(name = "concurrent_chemotherapy")
    private String concurrentChemotherapy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_CONCURRENT_CHEMOTHERAPY_LOOKUP_CLINIC_ID)
    )
    private Clinic clinic;
}
