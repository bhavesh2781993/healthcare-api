package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.TreatmentStageModel;
import com.test.api.healthcare.configurations.models.entities.TreatmentStage;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface TreatmentStageMapper {

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "treatmentStageId", source = "id")
    TreatmentStageModel toTreatmentStageModel(TreatmentStage treatmentStage);

    List<TreatmentStageModel> toTreatmentStageModelList(List<TreatmentStage> treatmentStages);

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "treatmentStageId")
    TreatmentStage toTreatmentStage(TreatmentStageModel treatmentStageModel);

    default TreatmentStage toTreatmentStage(TreatmentStageModel treatmentStageModel, Long clinicId) {
        treatmentStageModel.setClinicId(clinicId);
        return toTreatmentStage(treatmentStageModel);
    }
}
