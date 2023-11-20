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
import com.test.api.healthcare.configurations.mappers.ClinicDepartmentMapper;
import com.test.api.healthcare.configurations.models.ClinicDepartmentModel;
import com.test.api.healthcare.configurations.models.entities.ClinicDepartment;
import com.test.api.healthcare.configurations.services.ClinicDepartmentService;

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
class ClinicDepartmentControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final Long HEADER_CLINIC_ID_VALUE = 1L;
    private static final String URI_DEPARTMENTS = "/departments";
    private static final String URI_CLINIC_DEPARTMENTS_ID = URI_DEPARTMENTS + "/{clinicDepartmentId}";
    private static final Long CLINIC_DEPARTMENT_ID = 1L;
    private static final String CLINIC_DEPARTMENT_NAME = "Test Department";
    private static final Long CLINIC_LOCATION_ID = 1L;

    @MockBean
    private ClinicDepartmentMapper clinicDepartmentMapper;

    @MockBean
    private ClinicDepartmentService clinicDepartmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClinicDepartment() throws Exception {
        when(clinicDepartmentMapper.toClinicDepartmentModel(any())).thenReturn(getMockedClinicDepartmentModel(CLINIC_DEPARTMENT_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_DEPARTMENTS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedClinicDepartmentModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("departmentName").description("Department name"),
                    fieldWithPath("clinicLocationId").description("Clinic Location Id")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void getClinicDepartment() throws Exception {
        when(clinicDepartmentService.getClinicDepartment(anyLong()))
            .thenReturn(Optional.of(getMockedClinicDepartment(CLINIC_DEPARTMENT_ID)));
        when(clinicDepartmentMapper.toClinicDepartmentModel(any())).thenReturn(getMockedClinicDepartmentModel(CLINIC_DEPARTMENT_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_DEPARTMENTS_ID, CLINIC_DEPARTMENT_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicDepartmentId").description("Clinic Department Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listClinicDepartments() throws Exception {
        when(clinicDepartmentService.listClinicDepartments(any())).thenReturn(paginatedMockedClinicDepartments(CLINIC_DEPARTMENT_ID));
        when(clinicDepartmentMapper.toClinicDepartmentModelList(any()))
            .thenReturn(List.of(getMockedClinicDepartmentModel(CLINIC_DEPARTMENT_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_DEPARTMENTS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParams(getMockedPaginationRequest())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
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
    void deleteClinicDepartment() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CLINIC_DEPARTMENTS_ID, CLINIC_DEPARTMENT_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicDepartmentId").description("Clinic Department Id")
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

    private ClinicDepartment getMockedClinicDepartment(final Long clinicDepartmentId) {
        final ClinicDepartmentModel mockedClinicDepartmentModel = getMockedClinicDepartmentModel(clinicDepartmentId);
        final ClinicDepartment clinicDepartment = new ClinicDepartment();
        clinicDepartment.setId(mockedClinicDepartmentModel.getClinicDepartmentId());
        return clinicDepartment;
    }

    private ClinicDepartmentModel getMockedClinicDepartmentModel(final Long clinicDepartmentId) {
        final ClinicDepartmentModel clinicDepartmentModel = new ClinicDepartmentModel();
        clinicDepartmentModel.setClinicDepartmentId(clinicDepartmentId);
        clinicDepartmentModel.setDepartmentName(CLINIC_DEPARTMENT_NAME);
        clinicDepartmentModel.setClinicLocationId(CLINIC_LOCATION_ID);
        return clinicDepartmentModel;
    }

    private Page<ClinicDepartment> paginatedMockedClinicDepartments(final Long clinicDepartmentId) {
        return new PageImpl<>(List.of(getMockedClinicDepartment(clinicDepartmentId)));
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
