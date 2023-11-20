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
import com.test.api.healthcare.configurations.constants.TreatmentMetaType;
import com.test.api.healthcare.configurations.mappers.TreatmentMetaMapper;
import com.test.api.healthcare.configurations.models.TreatmentMetaModel;
import com.test.api.healthcare.configurations.services.TreatmentMetaService;

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
class TreatmentMetaControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_CONFIGURATIONS_TREATMENT_META = "/configurations/treatment-meta";
    private static final String URI_CONFIGURATIONS_TREATMENT_META_ID = URI_CONFIGURATIONS_TREATMENT_META + "/{treatmentMetaId}";
    private static final Long TREATMENT_META_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TreatmentMetaMapper treatmentMetaMapper;
    @MockBean
    private TreatmentMetaService treatmentMetaService;

    @Test
    void createTreatmentMeta() throws Exception {
        when(treatmentMetaMapper.toTreatmentMetaModel(any())).thenReturn(getMockedTreatmentMetaModel(TREATMENT_META_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_CONFIGURATIONS_TREATMENT_META)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedTreatmentMetaModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("metaValue").description("TreatmentMeta value"),
                    fieldWithPath("metaType").description("Meta type. Allowed values :" + TreatmentMetaType.getAllowedValues())
                )))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listTreatmentMetas() throws Exception {
        when(treatmentMetaMapper
            .toTreatmentMetaModelList(any()))
            .thenReturn(List.of(getMockedTreatmentMetaModel(TREATMENT_META_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CONFIGURATIONS_TREATMENT_META)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParam("metaType", "TREATMENT_SITE")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                queryParameters(
                    parameterWithName("metaType").description("Meta type. Allowed values :" + TreatmentMetaType.getAllowedValues())
                )))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void deleteTreatmentMeta() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CONFIGURATIONS_TREATMENT_META_ID, TREATMENT_META_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("treatmentMetaId").description("TreatmentMeta id")
                )))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic id")
        );
    }

    private TreatmentMetaModel getMockedTreatmentMetaModel(final Long treatmentMetaId, final Long clinicId) {
        final TreatmentMetaModel treatmentMetaModel = new TreatmentMetaModel();
        treatmentMetaModel.setTreatmentMetaId(treatmentMetaId);
        treatmentMetaModel.setClinicId(clinicId);
        treatmentMetaModel.setMetaValue("TREATMENT_SITE OUR TYPE");
        treatmentMetaModel.setMetaType(TreatmentMetaType.TREATMENT_SITE.name());
        return treatmentMetaModel;
    }
}
