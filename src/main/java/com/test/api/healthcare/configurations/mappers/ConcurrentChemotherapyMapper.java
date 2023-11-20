package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ConcurrentChemotherapyModel;
import com.test.api.healthcare.configurations.models.entities.ConcurrentChemotherapy;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ConcurrentChemotherapyMapper {

    @Mapping(target = "id", source = "concurrentChemotherapyId")
    @Mapping(target = "clinic.id", source = "clinicId")
    ConcurrentChemotherapy toConcurrentChemotherapy(ConcurrentChemotherapyModel concurrentChemotherapyModel);

    default ConcurrentChemotherapy toConcurrentChemotherapy(ConcurrentChemotherapyModel concurrentChemotherapyModel, Long clinicId) {
        concurrentChemotherapyModel.setClinicId(clinicId);
        return toConcurrentChemotherapy(concurrentChemotherapyModel);
    }

    @Mapping(target = "concurrentChemotherapyId", source = "id")
    @Mapping(target = "clinicId", source = "clinic.id")
    ConcurrentChemotherapyModel toConcurrentChemotherapyModel(ConcurrentChemotherapy concurrentChemotherapy);

    List<ConcurrentChemotherapyModel> toConcurrentChemotherapyModelList(List<ConcurrentChemotherapy> concurrentChemotherapyList);
}
