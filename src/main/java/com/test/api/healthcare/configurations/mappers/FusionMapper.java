package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.FusionModel;
import com.test.api.healthcare.configurations.models.entities.Fusion;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface FusionMapper {

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "fusionId")
    Fusion toFusion(FusionModel fusionModel);

    default Fusion toFusion(FusionModel fusionModel, Long clinicId) {
        fusionModel.setClinicId(clinicId);
        return toFusion(fusionModel);
    }

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "fusionId", source = "id")
    FusionModel toFusionModel(Fusion fusion);

    List<FusionModel> toFusionModelList(List<Fusion> fusionList);

    List<Fusion> toFusionList(List<FusionModel> fusionModelList);
}
