package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.TreatmentMetaModel;
import com.test.api.healthcare.configurations.models.entities.TreatmentMeta;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))

public interface TreatmentMetaMapper {

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "treatmentMetaId", source = "id")
    TreatmentMetaModel toTreatmentMetaModel(TreatmentMeta treatmentMeta);

    List<TreatmentMetaModel> toTreatmentMetaModelList(List<TreatmentMeta> treatmentMetas);

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "treatmentMetaId")
    TreatmentMeta toTreatmentMeta(TreatmentMetaModel treatmentMetaModel);

    default TreatmentMeta toTreatmentMeta(TreatmentMetaModel treatmentMetaModel, Long clinicId) {
        treatmentMetaModel.setClinicId(clinicId);
        return toTreatmentMeta(treatmentMetaModel);
    }

    List<TreatmentMeta> toTreatmentMetaList(List<TreatmentMetaModel> treatmentMetaModels);
}
