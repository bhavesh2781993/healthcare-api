package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.PositionModel;
import com.test.api.healthcare.configurations.models.entities.Position;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface PositionMapper {

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "positionId", source = "id")
    PositionModel toPositionModel(Position position);

    List<PositionModel> toPositionModelList(List<Position> positionList);

    List<Position> toPositionList(List<PositionModel> positionModelList);

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "positionId")
    Position toPosition(PositionModel positionModel);

    default Position toPosition(PositionModel positionModel, Long clinicId) {
        positionModel.setClinicId(clinicId);
        return toPosition(positionModel);
    }
}
