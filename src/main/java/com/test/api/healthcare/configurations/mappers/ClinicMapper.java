package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ClinicModel;
import com.test.api.healthcare.configurations.models.entities.Clinic;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ClinicMapper {

    @Mapping(target = "id", source = "clinicId")
    Clinic clinicModelToClinic(ClinicModel clinicModel);

    @Mapping(target = "clinicId", source = "id")
    ClinicModel clinicToClinicModel(Clinic clinic);

    List<ClinicModel> toClinicModelList(List<Clinic> clinicList);
}
