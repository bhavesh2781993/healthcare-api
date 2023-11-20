package com.test.api.healthcare.configurations.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.mappers.MedicalPhysicsConsultMapper;
import com.test.api.healthcare.configurations.models.MedicalPhysicsConsultModel;
import com.test.api.healthcare.configurations.services.MedicalPhysicsConsultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class MedicalPhysicsConsultControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_MEDICAL_PHYSICS_CONSULT = "/configurations/medical-physics-consults";
    private static final String URI_MEDICAL_PHYSICS_CONSULT_ID = URI_MEDICAL_PHYSICS_CONSULT + "/{medicalPhysicsConsultId}";
    private static final Long MEDICAL_PHYSICS_CONSULT_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalPhysicsConsultMapper medicalPhysicsConsultMapper;

    @MockBean
    private MedicalPhysicsConsultService medicalPhysicsConsultService;

    @Test
    void createMedicalPhysicsConsult() throws Exception {
        when(medicalPhysicsConsultMapper
            .toMedicalPhysicsConsultModel(any()))
            .thenReturn(getMockedMedicalPhysicsConsultModel(MEDICAL_PHYSICS_CONSULT_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_MEDICAL_PHYSICS_CONSULT)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedMedicalPhysicsConsultModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("consult").description("Medical Physics Consult")
                )))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listMedicalPhysicsConsults() throws Exception {
        when(medicalPhysicsConsultMapper
            .toMedicalPhysicsConsultModelList(any()))
            .thenReturn(List.of(getMockedMedicalPhysicsConsultModel(MEDICAL_PHYSICS_CONSULT_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_MEDICAL_PHYSICS_CONSULT)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet()
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void deleteMedicalPhysicsConsult() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_MEDICAL_PHYSICS_CONSULT_ID, MEDICAL_PHYSICS_CONSULT_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("medicalPhysicsConsultId").description("Medical Physics Consult Id")
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

    private MedicalPhysicsConsultModel getMockedMedicalPhysicsConsultModel(final Long medicalPhysicsConsultId, final Long clinicId) {
        final MedicalPhysicsConsultModel medicalPhysicsConsultModel = new MedicalPhysicsConsultModel();
        medicalPhysicsConsultModel.setMedicalPhysicsConsultId(medicalPhysicsConsultId);
        medicalPhysicsConsultModel.setClinicId(clinicId);
        medicalPhysicsConsultModel.setConsult("Medical Physics Consult");
        return medicalPhysicsConsultModel;
    }

}
