package com.test.api.healthcare.configurations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.configurations.models.entities.Workflow;
import com.test.api.healthcare.configurations.repositories.WorkflowRepository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowServiceTest {

    private static final Long WORKFLOW_ID = 1L;
    private static final String WORKFLOW_NAME = "Test Workflow";

    @Mock
    private WorkflowRepository workflowRepository;

    @InjectMocks
    private WorkflowService workflowService;


    @Test
    void createWorkflow() {
        final Workflow workflow = getMockedWorkflow();
        when(workflowRepository.save(any())).thenReturn(getMockedWorkflow());
        final Workflow response = workflowService.createWorkflow(workflow);
        assertNotNull(response);
    }

    @Test
    void getWorkflow() {
        final Workflow workflow = getMockedWorkflow();
        when(workflowRepository.findById(workflow.getId())).thenReturn(Optional.of(workflow));
        final Optional<Workflow> response = workflowService.getWorkflow(workflow.getId());
        assertTrue(response.isPresent());
    }

    @Test
    void deleteWorkflow() {
        final Workflow workflow = getMockedWorkflow();
        when(workflowRepository.findById(workflow.getId())).thenReturn(Optional.of(workflow));
        workflowService.deleteWorkflow(workflow.getId());
        verify(workflowRepository, times(1)).findById(WORKFLOW_ID);
    }

    @Test
    void listClinics() {
        final List<Workflow> workflowList = listMockedWorkflows();
        when(workflowRepository.findAll()).thenReturn(workflowList);
        final List<Workflow> resultPage = workflowService.listWorkflows();

        assertEquals(workflowList.size(), resultPage.size());
    }

    private Workflow getMockedWorkflow() {
        final Workflow workflow = new Workflow();
        workflow.setId(WORKFLOW_ID);
        workflow.setName(WORKFLOW_NAME);
        return workflow;
    }

    private List<Workflow> listMockedWorkflows() {
        return List.of(getMockedWorkflow());
    }

}

