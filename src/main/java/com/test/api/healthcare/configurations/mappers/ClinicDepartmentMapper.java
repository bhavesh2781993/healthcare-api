package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ClinicDepartmentModel;
import com.test.api.healthcare.configurations.models.entities.ClinicDepartment;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ClinicDepartmentMapper {

    @Mapping(target = "clinicLocation.id", source = "clinicLocationId")
    @Mapping(target = "id", source = "clinicDepartmentId")
    ClinicDepartment toClinicDepartment(ClinicDepartmentModel clinicDepartmentModel);

    List<ClinicDepartment> toClinicDepartmentList(List<ClinicDepartmentModel> clinicDepartmentModels);

    @Mapping(target = "clinicLocationId", source = "clinicLocation.id")
    @Mapping(target = "clinicDepartmentId", source = "id")
    ClinicDepartmentModel toClinicDepartmentModel(ClinicDepartment clinicDepartment);

    List<ClinicDepartmentModel> toClinicDepartmentModelList(List<ClinicDepartment> clinicDepartments);
}
