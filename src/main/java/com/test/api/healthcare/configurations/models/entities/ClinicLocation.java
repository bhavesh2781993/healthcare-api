package com.test.api.healthcare.configurations.models.entities;

import com.test.api.healthcare.common.models.entities.Auditable;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "clinic_locations")
public class ClinicLocation extends Auditable {

    public static final String FK_CLINIC_LOCATION_CLINIC_ID = "fk_clinic_location_clinic_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clinic_locations")
    @SequenceGenerator(name = "seq_clinic_locations", allocationSize = 5)
    @Column(name = "clinic_location_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_id",
        referencedColumnName = "clinic_id",
        foreignKey = @ForeignKey(name = FK_CLINIC_LOCATION_CLINIC_ID))
    private Clinic clinic;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "timezone")
    private String timezone;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "clinicLocation")
    private List<ClinicDepartment> clinicDepartments;
}
