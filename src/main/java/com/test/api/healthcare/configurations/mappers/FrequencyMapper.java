package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.FrequencyModel;
import com.test.api.healthcare.configurations.models.entities.Frequency;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface FrequencyMapper {

    @Mapping(target = "id", source = "frequencyId")
    @Mapping(target = "clinic.id", source = "clinicId")
    Frequency toFrequency(FrequencyModel frequencyModel);

    default Frequency toFrequency(FrequencyModel frequencyModel, Long clinicId) {
        frequencyModel.setClinicId(clinicId);
        return toFrequency(frequencyModel);
    }

    @Mapping(target = "frequencyId", source = "id")
    @Mapping(target = "clinicId", source = "clinic.id")
    FrequencyModel toFrequencyModel(Frequency frequency);

    List<FrequencyModel> toFrequencyModelList(List<Frequency> frequencyList);

}
