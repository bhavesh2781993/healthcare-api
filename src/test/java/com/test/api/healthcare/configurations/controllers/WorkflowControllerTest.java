package com.test.api.healthcare.configurations.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.constants.WorkflowType;
import com.test.api.healthcare.configurations.mappers.WorkflowMapper;
import com.test.api.healthcare.configurations.models.WorkflowModel;
import com.test.api.healthcare.configurations.models.entities.Workflow;
import com.test.api.healthcare.configurations.services.WorkflowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class WorkflowControllerTest extends BaseDocumentationTest {

    private static final String URI_WORKFLOWS = "/workflows";
    private static final String URI_WORKFLOWS_ID = URI_WORKFLOWS + "/{workflowId}";
    private static final Long WORKFLOW_ID = 1L;
    private static final String WORKFLOW_NAME = "Test Workflow";

    @MockBean
    private WorkflowMapper workflowMapper;

    @MockBean
    private WorkflowService workflowService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createWorkflow() throws Exception {
        when(workflowMapper.toWorkflowModel(any())).thenReturn(getMockedWorkflowModel(WORKFLOW_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_WORKFLOWS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedWorkflowModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("name").description("Workflow name"),
                    fieldWithPath("type").description("Workflow Type")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void getWorkflow() throws Exception {
        when(workflowService.getWorkflow(anyLong())).thenReturn(Optional.of(getMockedWorkflow(WORKFLOW_ID)));
        when(workflowMapper.toWorkflowModel(any())).thenReturn(getMockedWorkflowModel(WORKFLOW_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_WORKFLOWS_ID, WORKFLOW_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("workflowId").description("Workflow Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listWorkflows() throws Exception {
        when(workflowService.listWorkflows()).thenReturn(getMockedWorkflows(WORKFLOW_ID));
        when(workflowMapper.toWorkflowModelList(any())).thenReturn(List.of(getMockedWorkflowModel(WORKFLOW_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_WORKFLOWS)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document())
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void deleteWorkflow() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_WORKFLOWS_ID, WORKFLOW_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("workflowId").description("Workflow Id")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private Workflow getMockedWorkflow(final Long workflowId) {
        final WorkflowModel mockedWorkflowModel = getMockedWorkflowModel(workflowId);
        final Workflow workflow = new Workflow();
        workflow.setId(mockedWorkflowModel.getWorkflowId());
        return workflow;
    }

    private WorkflowModel getMockedWorkflowModel(final Long workflowId) {
        final WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setName(WORKFLOW_NAME);
        workflowModel.setType(WorkflowType.REGULAR.name());
        workflowModel.setSteps(new ArrayList<>());
        return workflowModel;
    }

    private List<Workflow> getMockedWorkflows(final Long workflowId) {
        return List.of(getMockedWorkflow(workflowId));
    }

    
}
