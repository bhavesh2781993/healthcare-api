package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.MedicalPhysicsConsultModel;
import com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface MedicalPhysicsConsultMapper {

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "medicalPhysicsConsultId")
    MedicalPhysicsConsult toMedicalPhysicsConsult(MedicalPhysicsConsultModel medicalPhysicsConsultModel);

    default MedicalPhysicsConsult toMedicalPhysicsConsult(MedicalPhysicsConsultModel medicalPhysicsConsultModel, Long clinicId) {
        medicalPhysicsConsultModel.setClinicId(clinicId);
        return toMedicalPhysicsConsult(medicalPhysicsConsultModel);
    }

    List<MedicalPhysicsConsult> toMedicalPhysicsConsultList(List<MedicalPhysicsConsultModel> medicalPhysicsConsultModelList);

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "medicalPhysicsConsultId", source = "id")
    MedicalPhysicsConsultModel toMedicalPhysicsConsultModel(MedicalPhysicsConsult medicalPhysicsConsult);

    List<MedicalPhysicsConsultModel> toMedicalPhysicsConsultModelList(List<MedicalPhysicsConsult> medicalPhysicsConsultsList);
}

