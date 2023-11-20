package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.ClinicWorkflowModel;
import com.test.api.healthcare.configurations.models.entities.ClinicWorkflow;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface ClinicWorkflowMapper {

    @Mapping(target = "clinic.id", source = "clinicId")
    @Mapping(target = "workflow.id", source = "workflowId")
    @Mapping(target = "id", source = "clinicWorkflowId")
    ClinicWorkflow toClinicWorkflow(ClinicWorkflowModel clinicWorkflowModel);

    List<ClinicWorkflow> toClinicWorkflowList(List<ClinicWorkflowModel> clinicWorkflowModels);

    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "workflowId", source = "workflow.id")
    @Mapping(target = "clinicWorkflowId", source = "id")
    ClinicWorkflowModel toClinicWorkflowModel(ClinicWorkflow clinicWorkflow);

    List<ClinicWorkflowModel> toClinicWorkflowModelList(List<ClinicWorkflow> clinicWorkflows);


}
