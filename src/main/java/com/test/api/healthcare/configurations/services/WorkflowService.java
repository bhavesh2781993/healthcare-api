package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_WORKFLOW_EXIST;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_WORKFLOW_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_WORKFLOW_SEQ_EXIST;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_WORKFLOW_STEP_EXIST;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.configurations.models.entities.Workflow;
import com.test.api.healthcare.configurations.models.entities.WorkflowStep;
import com.test.api.healthcare.configurations.repositories.WorkflowRepository;
import com.test.api.healthcare.configurations.repositories.WorkflowStepRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowStepRepository workflowStepRepository;

    public Workflow createWorkflow(final Workflow workflowToCreate) {
        workflowToCreate.addWorkflowSteps();
        try {
            return workflowRepository.save(workflowToCreate);
        } catch (final DataIntegrityViolationException e) {
            if (e.getMessage().contains("uk_workflow_name_type")) {
                throw new DataValidationException(ERR_MSG_WORKFLOW_EXIST);
            } else if (e.getMessage().contains("uk_workflow_step_workflow_step")) {
                throw new DataValidationException(ERR_MSG_WORKFLOW_STEP_EXIST);
            } else if (e.getMessage().contains("uk_workflow_step_workflow_seq")) {
                throw new DataValidationException(ERR_MSG_WORKFLOW_SEQ_EXIST);
            }
            throw new DataValidationException("Data Validation Exception");
        }
    }

    public Optional<Workflow> getWorkflow(final Long workflowId) {
        return workflowRepository.findById(workflowId);
    }

    public List<Workflow> listWorkflows() {
        return workflowRepository.findAll();
    }

    public void deleteWorkflow(final Long workflowId) {
        final Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_WORKFLOW_NOT_FOUND, workflowId)));
        workflowRepository.delete(workflow);
    }

    public Optional<WorkflowStep> getNextWorkflowStep(final WorkflowStep workflowStep) {
        final Workflow workflow = workflowStep.getWorkflow();
        final Integer currentStepSeq = workflowStep.getSeq();

        return workflowStepRepository.findWorkflowStep(workflow.getId(), currentStepSeq + 1);
    }

}
