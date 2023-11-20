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
@Table(name = "instructions_lookup")
public class Instruction extends Auditable {

    public static final String FK_INSTRUCTION_LOOKUP_CLINIC_ID = "fk_instruction_lookup_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_instructions_lookup")
    @SequenceGenerator(name = "seq_instructions_lookup", allocationSize = 5)
    @Column(name = "instruction_lookup_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_INSTRUCTION_LOOKUP_CLINIC_ID)
    )
    private Clinic clinic;

    private String instruction;
}
