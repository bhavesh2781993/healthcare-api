package com.test.api.healthcare.configurations.mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.api.healthcare.configurations.models.WorkflowModel;
import com.test.api.healthcare.configurations.models.WorkflowStepModel;
import com.test.api.healthcare.configurations.models.entities.Workflow;
import com.test.api.healthcare.configurations.models.entities.WorkflowStep;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface WorkflowMapper {

    @Mapping(target = "id", source = "workflowId")
    @Mapping(target = "workflowSteps", source = "steps")
    Workflow toWorkflow(WorkflowModel workflowModel);

    List<Workflow> toWorkflowList(List<WorkflowModel> workflowModels);

    @Mapping(target = "workflowId", source = "id")
    @Mapping(target = "steps", source = "workflowSteps")
    WorkflowModel toWorkflowModel(Workflow workflow);

    List<WorkflowModel> toWorkflowModelList(List<Workflow> workflows);



    @Mapping(target = "workflow.id", source = "workflowId")
    @Mapping(target = "id", source = "workflowStepId")
    WorkflowStep toWorkflowStep(WorkflowStepModel workflowStepModel);

    List<WorkflowStep> toWorkflowStepList(List<WorkflowStepModel> workflowStepModels);

    @Mapping(target = "workflowId", source = "workflow.id")
    @Mapping(target = "workflowStepId", source = "id")
    WorkflowStepModel toWorkflowStepModel(WorkflowStep workflowStep);

    List<WorkflowStepModel> toWorkStepModelList(List<WorkflowStep> workflowSteps);

}
