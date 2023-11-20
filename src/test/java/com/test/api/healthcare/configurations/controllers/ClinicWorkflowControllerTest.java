package com.test.api.healthcare.configurations.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.constants.WorkflowStatus;
import com.test.api.healthcare.configurations.mappers.ClinicWorkflowMapper;
import com.test.api.healthcare.configurations.models.ClinicWorkflowModel;
import com.test.api.healthcare.configurations.models.entities.ClinicWorkflow;
import com.test.api.healthcare.configurations.services.ClinicWorkflowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class ClinicWorkflowControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String URI_CLINIC_WORKFLOWS = "/clinic-workflows";
    private static final String URI_CLINIC_WORKFLOWS_ID = URI_CLINIC_WORKFLOWS + "/{clinicWorkflowId}";
    private static final Long CLINIC_WORKFLOW_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final Long WORKFLOW_ID = 1L;

    @MockBean
    private ClinicWorkflowMapper clinicWorkflowMapper;

    @MockBean
    private ClinicWorkflowService clinicWorkflowService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClinicWorkflow() throws Exception {
        when(clinicWorkflowMapper.toClinicWorkflowModel(any())).thenReturn(getMockedClinicWorkflowModel(CLINIC_WORKFLOW_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_CLINIC_WORKFLOWS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedClinicWorkflowModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("clinicId").description("Clinic Id"),
                    fieldWithPath("workflowId").description("Workflow Id"),
                    fieldWithPath("status").description("Clinic Workflow Status")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void getClinicWorkflow() throws Exception {
        when(clinicWorkflowService.getClinicWorkflow(anyLong()))
            .thenReturn(Optional.of(getMockedClinicWorkflow(CLINIC_WORKFLOW_ID)));
        when(clinicWorkflowMapper.toClinicWorkflowModel(any())).thenReturn(getMockedClinicWorkflowModel(CLINIC_WORKFLOW_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_WORKFLOWS_ID, CLINIC_WORKFLOW_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicWorkflowId").description("Clinic workflow Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listClinicWorkflows() throws Exception {
        when(clinicWorkflowService.listClinicWorkflows(any())).thenReturn(paginatedMockedClinicWorkflows(CLINIC_WORKFLOW_ID));
        when(clinicWorkflowMapper.toClinicWorkflowModelList(any()))
            .thenReturn(List.of(getMockedClinicWorkflowModel(CLINIC_WORKFLOW_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_WORKFLOWS)
                .queryParams(getMockedPaginationRequest())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                queryParameters(
                    parameterWithName("pageNo").description("Page no for which resources are requested. pageNo starts at: 0"),
                    parameterWithName("pageSize").description("No of elements in a page. Max pageSize limit: 100"),
                    parameterWithName("sortField").description("Field on which sorting is requested"),
                    parameterWithName("sortDirection").description("Direction of sort field. Allowed values: [ASC, DESC]")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void deleteClinicWorkflow() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CLINIC_WORKFLOWS_ID, CLINIC_WORKFLOW_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicWorkflowId").description("Clinic Workflow Id")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic id")
        );
    }

    private ClinicWorkflow getMockedClinicWorkflow(final Long clinicWorkflowId) {
        final ClinicWorkflowModel mockedClinicWorkflowModel = getMockedClinicWorkflowModel(clinicWorkflowId);
        final ClinicWorkflow clinicWorkflow = new ClinicWorkflow();
        clinicWorkflow.setId(mockedClinicWorkflowModel.getClinicWorkflowId());
        return clinicWorkflow;
    }

    private ClinicWorkflowModel getMockedClinicWorkflowModel(final Long clinicWorkflowId) {
        final ClinicWorkflowModel clinicWorkflowModel = new ClinicWorkflowModel();
        clinicWorkflowModel.setClinicWorkflowId(clinicWorkflowId);
        clinicWorkflowModel.setClinicId(CLINIC_ID);
        clinicWorkflowModel.setWorkflowId(WORKFLOW_ID);
        clinicWorkflowModel.setStatus(WorkflowStatus.ACTIVE.name());
        return clinicWorkflowModel;
    }

    private Page<ClinicWorkflow> paginatedMockedClinicWorkflows(final Long clinicWorkflowId) {
        return new PageImpl<>(List.of(getMockedClinicWorkflow(clinicWorkflowId)));
    }

    private MultiValueMap<String, String> getMockedPaginationRequest() {
        final LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("pageNo", "0");
        linkedMultiValueMap.add("pageSize", "1");
        linkedMultiValueMap.add("sortField", "name");
        linkedMultiValueMap.add("sortDirection", "asc");
        return linkedMultiValueMap;
    }

}
