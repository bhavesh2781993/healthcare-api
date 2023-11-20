package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.EnergyModel;
import com.test.api.healthcare.configurations.models.entities.Energy;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface EnergyMapper {

    @Mapping(target = "id", source = "energyId")
    @Mapping(target = "clinic.id", source = "clinicId")
    Energy toEnergy(EnergyModel energyModel);

    default Energy toEnergy(EnergyModel energyModel, Long clinicId) {
        energyModel.setClinicId(clinicId);
        return toEnergy(energyModel);
    }

    @Mapping(target = "energyId", source = "id")
    @Mapping(target = "clinicId", source = "clinic.id")
    EnergyModel toEnergyModel(Energy energy);

    List<EnergyModel> toEnergyModelList(List<Energy> energyList);

}
