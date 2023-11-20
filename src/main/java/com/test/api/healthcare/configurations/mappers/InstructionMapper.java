package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.InstructionModel;
import com.test.api.healthcare.configurations.models.entities.Instruction;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface InstructionMapper {


    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "instructionsId", source = "id")
    InstructionModel toInstructionModel(Instruction instruction);

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "id", source = "instructionsId")
    Instruction toInstruction(InstructionModel instructionModel);

    default Instruction toInstruction(InstructionModel instructionModel, Long clinicId) {
        instructionModel.setClinicId(clinicId);
        return toInstruction(instructionModel);
    }

    List<Instruction> toInstructionList(List<InstructionModel> instructionModels);

    List<InstructionModel> toInstructionModelList(List<Instruction> instructions);

}
