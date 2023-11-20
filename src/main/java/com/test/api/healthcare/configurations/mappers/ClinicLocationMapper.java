package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ClinicLocationModel;
import com.test.api.healthcare.configurations.models.entities.ClinicLocation;
import com.test.api.healthcare.configurations.models.entities.ClinicUser;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ClinicLocationMapper {

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "clinicLocationId")
    ClinicLocation toClinicLocation(ClinicLocationModel clinicLocationModel);

    default ClinicLocation toClinicLocation(ClinicLocationModel clinicLocationModel, Long clinicId) {
        clinicLocationModel.setClinicId(clinicId);
        return toClinicLocation(clinicLocationModel);
    }

    List<ClinicUser> toClinicLocationList(List<ClinicLocationModel> clinicLocationModels);

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "clinicLocationId", source = "id")
    ClinicLocationModel toClinicLocationModel(ClinicLocation clinicLocation);

    List<ClinicLocationModel> toClinicLocationModelList(List<ClinicLocation> clinicLocations);
}
