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
@Table(name = "clinic_departments")
public class ClinicDepartment extends Auditable {

    public static final String FK_CLINIC_DEPARTMENT_CLINIC_LOCATION_ID = "fk_clinic_department_clinic_location_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clinic_departments")
    @SequenceGenerator(name = "seq_clinic_departments", allocationSize = 5)
    @Column(name = "clinic_department_id")
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_location_id",
        referencedColumnName = "clinic_location_id",
        foreignKey = @ForeignKey(name = FK_CLINIC_DEPARTMENT_CLINIC_LOCATION_ID))
    private ClinicLocation clinicLocation;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "clinicDepartment")
    private List<ClinicUser> clinicUsers;

}
