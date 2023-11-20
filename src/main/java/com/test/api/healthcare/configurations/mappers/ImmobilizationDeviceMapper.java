package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ImmobilizationDeviceModel;
import com.test.api.healthcare.configurations.models.entities.ImmobilizationDevice;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))

public interface ImmobilizationDeviceMapper {

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "immobilizationDeviceId", source = "id")
    ImmobilizationDeviceModel toImmobilizationDeviceModel(ImmobilizationDevice immobilizationDevice);

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "immobilizationDeviceId")
    ImmobilizationDevice toImmobilizationDevice(ImmobilizationDeviceModel immobilizationDeviceModel);

    default ImmobilizationDevice toImmobilizationDevice(ImmobilizationDeviceModel immobilizationDeviceModel, Long clinicId) {
        immobilizationDeviceModel.setClinicId(clinicId);
        return toImmobilizationDevice(immobilizationDeviceModel);
    }

    List<ImmobilizationDeviceModel> toImmobilizationDeviceModelList(List<ImmobilizationDevice> immobilizationDevice);

}

