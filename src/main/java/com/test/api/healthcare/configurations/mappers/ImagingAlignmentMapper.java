package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ImagingAlignmentModel;
import com.test.api.healthcare.configurations.models.entities.ImagingAlignment;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ImagingAlignmentMapper {

    @Mapping(target = "id", source = "imagingAlignmentId")
    @Mapping(target = "clinic.id", source = "clinicId")
    ImagingAlignment toImagingAlignment(ImagingAlignmentModel imagingAlignmentModel);

    default ImagingAlignment toImagingAlignment(ImagingAlignmentModel imagingAlignmentModel, Long clinicId) {
        imagingAlignmentModel.setClinicId(clinicId);
        return toImagingAlignment(imagingAlignmentModel);
    }

    @Mapping(target = "imagingAlignmentId", source = "id")
    @Mapping(target = "clinicId", source = "clinic.id")
    ImagingAlignmentModel toImagingAlignmentModel(ImagingAlignment imagingAlignment);

    List<ImagingAlignment> toImagingAlignmentList(List<ImagingAlignment> imagingAlignments);

    List<ImagingAlignmentModel> toImagingAlignmentModelList(List<ImagingAlignment> imagingAlignments);
}
