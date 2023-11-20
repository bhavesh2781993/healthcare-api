package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.TreatmentProcedureModel;
import com.test.api.healthcare.configurations.models.entities.TreatmentProcedure;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface TreatmentProcedureMapper {
    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "treatmentProcedureId", source = "id")
    TreatmentProcedureModel toTreatmentProcedureModel(TreatmentProcedure treatmentProcedure);

    default TreatmentProcedure toTreatmentProcedure(TreatmentProcedureModel treatmentProcedureModel, Long clinicId) {
        treatmentProcedureModel.setClinicId(clinicId);
        return toTreatmentProcedure(treatmentProcedureModel);
    }

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "treatmentProcedureId")
    TreatmentProcedure toTreatmentProcedure(TreatmentProcedureModel treatmentProcedureModel);

    List<TreatmentProcedure> toTreatmentProcedureList(List<TreatmentProcedureModel> treatmentProcedureModelList);

    List<TreatmentProcedureModel> toTreatmentProcedureModelList(List<TreatmentProcedure> treatmentProcedures);
}
