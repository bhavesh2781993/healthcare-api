package com.test.api.healthcare.configurations.models.entities;

import com.test.api.healthcare.common.models.entities.Auditable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Entity
@Table(name = "clinic_user_images")
public class ClinicUserImage extends Auditable {

    public static final String FK_CLINIC_IMAGE_CLINIC_USER_ID = "fk_clinic_user_image_clinic_user_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clinic_user_images")
    @SequenceGenerator(name = "seq_clinic_user_images", allocationSize = 5)
    @Column(name = "clinic_user_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "clinic_user_id",
        referencedColumnName = "clinic_user_id",
        foreignKey = @ForeignKey(name = FK_CLINIC_IMAGE_CLINIC_USER_ID)
    )
    private ClinicUser clinicUser;

    private byte[] image;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "is_active")
    private Boolean isActive;
}
