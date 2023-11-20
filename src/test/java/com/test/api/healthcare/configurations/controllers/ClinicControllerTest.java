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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.mappers.ClinicMapper;
import com.test.api.healthcare.configurations.models.ClinicModel;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.services.ClinicService;
import com.test.api.healthcare.security.configs.RoleAuthorityEvaluator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class ClinicControllerTest extends BaseDocumentationTest {

    private static final String URI_CLINICS = "/clinics";
    private static final String URI_CLINIC_ID = URI_CLINICS + "/{id}";

    private static final Long CLINIC_ID = 1L;
    private static final String CLINIC_NAME = "test clinic";

    @MockBean
    private ClinicMapper clinicMapper;
    @MockBean
    private ClinicService clinicService;
    // Needed for authorization
    @MockBean
    private RoleAuthorityEvaluator roleAuthorityEvaluator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClinic() throws Exception {
        when(clinicMapper.clinicToClinicModel(any())).thenReturn(getMockedClinicModel(CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_CLINICS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedClinicModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("name").description("Clinic name")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getClinic() throws Exception {
        when(clinicService.getClinic(anyLong())).thenReturn(Optional.of(getMockedClinic(CLINIC_ID)));
        when(clinicMapper.clinicToClinicModel(any())).thenReturn(getMockedClinicModel(CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_ID, CLINIC_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("id").description("Clinic id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listClinics() throws Exception {
        when(clinicService.listClinics(any())).thenReturn(paginatedMockedClinics(CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINICS)
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
    void deleteClinic() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CLINIC_ID, CLINIC_ID))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("id").description("Clinic id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private ClinicModel getMockedClinicModel(final Long clinicId) {
        final ClinicModel clinicModel = new ClinicModel();
        clinicModel.setClinicId(clinicId);
        clinicModel.setName(CLINIC_NAME);
        return clinicModel;
    }

    private Clinic getMockedClinic(final Long clinicId) {
        final ClinicModel mockedClinicModel = getMockedClinicModel(clinicId);
        final Clinic clinic = new Clinic();
        clinic.setName(mockedClinicModel.getName());
        clinic.setId(mockedClinicModel.getClinicId());
        return clinic;
    }

    private List<Clinic> listMockedClinics(final Long clinicId) {
        return List.of(getMockedClinic(clinicId));
    }

    private Page<Clinic> paginatedMockedClinics(final Long clinicId) {
        return new PageImpl<>(listMockedClinics(clinicId));
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
