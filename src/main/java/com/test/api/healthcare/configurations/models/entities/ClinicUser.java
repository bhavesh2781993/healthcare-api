package com.test.api.healthcare.configurations.models.entities;

import com.test.api.healthcare.common.models.entities.Auditable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Stream;

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
@Table(name = "clinic_users")
public class ClinicUser extends Auditable {

    public static final String FK_CLINIC_USER_CLINIC_DEPARTMENT_ID = "fk_clinic_user_clinic_department_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clinic_users")
    @SequenceGenerator(name = "seq_clinic_users", allocationSize = 5)
    @Column(name = "clinic_user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "designation")
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        nullable = false,
        name = "clinic_department_id",
        referencedColumnName = "clinic_department_id",
        foreignKey = @ForeignKey(name = FK_CLINIC_USER_CLINIC_DEPARTMENT_ID))
    private ClinicDepartment clinicDepartment;

    private String phone;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    private String email;

    @Column(name = "alternate_email")
    private String alternateEmail;

    private String address;

    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "clinicUser",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    private List<ClinicUserImage> clinicUserImages;

    public void addClinicUserImages() {
        if (!CollectionUtils.isEmpty(clinicUserImages)) {
            clinicUserImages.forEach(clinicUserImage -> clinicUserImage.setClinicUser(this));
        }
    }

    public void addClinicUserImages(final List<ClinicUserImage> clinicUserImages) {
        this.clinicUserImages = Stream.concat(this.getClinicUserImages().stream(), clinicUserImages.stream()).toList();
        addClinicUserImages();
    }
}
