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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.constants.SpreadType;
import com.test.api.healthcare.configurations.constants.StageType;
import com.test.api.healthcare.configurations.mappers.TreatmentStageMapper;
import com.test.api.healthcare.configurations.models.TreatmentStageModel;
import com.test.api.healthcare.configurations.services.TreatmentStageService;

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
class TreatmentStageControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_CONFIGURATIONS_TREATMENT = "/configurations/treatment-stages";
    private static final String URI_CONFIGURATIONS_TREATMENT_ID = URI_CONFIGURATIONS_TREATMENT + "/{treatmentStageId}";
    private static final Long TREATMENT_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private TreatmentStageMapper treatmentStageMapper;

    @MockBean
    private TreatmentStageService treatmentStageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTreatmentStage() throws Exception {
        when(treatmentStageMapper.toTreatmentStageModel(any())).thenReturn(getMockedTreatmentModel(TREATMENT_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_CONFIGURATIONS_TREATMENT)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedTreatmentModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("stage").description("In stage"),
                    fieldWithPath("stageType").description("Stage type. Allowed values :" + StageType.getAllowedValues()),
                    fieldWithPath("spreadType").description("Spread type. Allowed values :" + SpreadType.getAllowedValues())
                )))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listTreatmentStages() throws Exception {
        when(treatmentStageMapper
            .toTreatmentStageModelList(any()))
            .thenReturn(List.of(getMockedTreatmentModel(TREATMENT_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CONFIGURATIONS_TREATMENT)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParam("stageType", "CLINICAL")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                queryParameters(
                    parameterWithName("stageType").description("Stage type. Allowed values :" + StageType.getAllowedValues())
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void deleteTreatmentStage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CONFIGURATIONS_TREATMENT_ID, TREATMENT_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("treatmentStageId").description("Treatment stage id")
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

    private TreatmentStageModel getMockedTreatmentModel(final Long treatmentId, final Long clinicId) {
        final TreatmentStageModel treatmentStageModel = new TreatmentStageModel();
        treatmentStageModel.setTreatmentStageId(treatmentId);
        treatmentStageModel.setClinicId(clinicId);
        treatmentStageModel.setStageType(StageType.CLINICAL.name());
        treatmentStageModel.setSpreadType(SpreadType.TUMOR.name());
        treatmentStageModel.setStage("Our Stage");
        return treatmentStageModel;
    }
}
