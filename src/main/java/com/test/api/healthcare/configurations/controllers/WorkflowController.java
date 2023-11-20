package com.test.api.healthcare.configurations.controllers;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_WORKFLOW_NOT_FOUND;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.responses.ApiResponse;
import com.test.api.healthcare.common.models.responses.PaginatedApiResponse;
import com.test.api.healthcare.configurations.mappers.WorkflowMapper;
import com.test.api.healthcare.configurations.models.WorkflowModel;
import com.test.api.healthcare.configurations.models.entities.Workflow;
import com.test.api.healthcare.configurations.services.WorkflowService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final WorkflowMapper workflowMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkflowModel>> createWorkflow(
        @RequestBody @Valid final WorkflowModel workflowModel) {
        final Workflow workflowToCreate = workflowMapper.toWorkflow(workflowModel);
        final Workflow createdWorkflow = workflowService.createWorkflow(workflowToCreate);
        final WorkflowModel createdWorkflowModel = workflowMapper.toWorkflowModel(createdWorkflow);
        final ApiResponse<WorkflowModel> response = ApiResponse.<WorkflowModel>builder()
            .data(createdWorkflowModel)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{workflowId}")
    public ResponseEntity<ApiResponse<WorkflowModel>> getWorkflow(@PathVariable("workflowId") final Long workflowId) {
        final Workflow matchingWorkflow = workflowService.getWorkflow(workflowId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_WORKFLOW_NOT_FOUND, workflowId)));
        final WorkflowModel matchingWorkflowModel = workflowMapper.toWorkflowModel(matchingWorkflow);
        final ApiResponse<WorkflowModel> response = ApiResponse.<WorkflowModel>builder()
            .data(matchingWorkflowModel)
            .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<PaginatedApiResponse<WorkflowModel>> listWorkflows() {
        final List<Workflow> workflowList = workflowService.listWorkflows();
        final List<WorkflowModel> workflowModelList = workflowMapper.toWorkflowModelList(workflowList);
        final PaginatedApiResponse<WorkflowModel> paginatedWorkflowResponse = PaginatedApiResponse.<WorkflowModel>builder()
            .data(workflowModelList)
            .build();
        return ResponseEntity.ok(paginatedWorkflowResponse);
    }

    @DeleteMapping("/{workflowId}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable("workflowId") final Long workflowId) {
        workflowService.deleteWorkflow(workflowId);
        return ResponseEntity.noContent().build();
    }


}
