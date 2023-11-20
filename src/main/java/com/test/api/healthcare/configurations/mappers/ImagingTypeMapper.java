package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ImagingTypeModel;
import com.test.api.healthcare.configurations.models.entities.ImagingType;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ImagingTypeMapper {

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "imagingTypeId", source = "id")
    ImagingTypeModel toImagingTypeModel(ImagingType imagingType);

    default ImagingType toImagingType(ImagingTypeModel imagingTypeModel, Long clinicId) {
        imagingTypeModel.setClinicId(clinicId);
        return toImagingType(imagingTypeModel);
    }

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "imagingTypeId")
    ImagingType toImagingType(ImagingTypeModel imagingTypeModel);

    List<ImagingTypeModel> toImagingTypeModelList(List<ImagingType> imagingTypeList);

}
