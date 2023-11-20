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
import com.test.api.healthcare.configurations.mappers.TreatmentProcedureMapper;
import com.test.api.healthcare.configurations.models.TreatmentProcedureModel;
import com.test.api.healthcare.configurations.services.TreatmentProcedureService;

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
class TreatmentProcedureControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_TREATMENT_PROCEDURE = "/configurations/treatment-procedures";
    private static final String URI_TREATMENT_PROCEDURE_ID = URI_TREATMENT_PROCEDURE + "/{treatmentProcedureId}";
    private static final Long TREATMENT_PROCEDURE_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TreatmentProcedureMapper treatmentProcedureMapper;

    @MockBean
    private TreatmentProcedureService treatmentProcedureService;

    @Test
    void createTreatmentProcedure() throws Exception {
        when(treatmentProcedureMapper
            .toTreatmentProcedureModel(any()))
            .thenReturn(getMockedTreatmentProcedureModel(TREATMENT_PROCEDURE_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_TREATMENT_PROCEDURE)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedTreatmentProcedureModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("procedure").description("Treatment Procedure ")
                )))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listTreatmentProcedures() throws Exception {
        when(treatmentProcedureMapper
            .toTreatmentProcedureModelList(any()))
            .thenReturn(List.of(getMockedTreatmentProcedureModel(TREATMENT_PROCEDURE_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_TREATMENT_PROCEDURE)
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
    void deleteTreatmentProcedure() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_TREATMENT_PROCEDURE_ID, TREATMENT_PROCEDURE_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("treatmentProcedureId").description("Treatment Procedure id"))
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic id")
        );
    }

    private TreatmentProcedureModel getMockedTreatmentProcedureModel(final Long treatmentProcedureId, final Long clinicId) {
        final TreatmentProcedureModel treatmentProcedureModel = new TreatmentProcedureModel();
        treatmentProcedureModel.setTreatmentProcedureId(treatmentProcedureId);
        treatmentProcedureModel.setClinicId(clinicId);
        treatmentProcedureModel.setProcedure("Procedure");
        return treatmentProcedureModel;
    }
}

