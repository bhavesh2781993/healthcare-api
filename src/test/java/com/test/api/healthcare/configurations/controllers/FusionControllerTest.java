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
import com.test.api.healthcare.configurations.mappers.FusionMapper;
import com.test.api.healthcare.configurations.models.FusionModel;
import com.test.api.healthcare.configurations.services.FusionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class FusionControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_CONFIGURATIONS_FUSION = "/configurations/fusions";
    private static final String URI_CONFIGURATIONS_FUSION_ID = URI_CONFIGURATIONS_FUSION + "/{fusionId}";
    private static final Long FUSION_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FusionMapper fusionMapper;

    @MockBean
    private FusionService fusionService;

    @Test
    void createFusion() throws Exception {
        when(fusionMapper
            .toFusionModel(any()))
            .thenReturn(getMockedFusionModel(FUSION_ID, CLINIC_ID));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
            .post(URI_CONFIGURATIONS_FUSION)
            .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
            .content(objectMapper.writeValueAsString(getMockedFusionModel(null, null)))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("fusions").description("Fusion 1"))
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listFusions() throws Exception {
        when(fusionMapper
            .toFusionModelList(any()))
            .thenReturn(List.of(getMockedFusionModel(FUSION_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
            .get(URI_CONFIGURATIONS_FUSION)
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
    void deleteFusion() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
            .delete(URI_CONFIGURATIONS_FUSION_ID, FUSION_ID)
            .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("fusionId").description("fusion Id")
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

    private FusionModel getMockedFusionModel(final Long fusionId, final Long clinicId) {
        final FusionModel fusionModel = new FusionModel();
        fusionModel.setFusionId(fusionId);
        fusionModel.setClinicId(clinicId);
        fusionModel.setFusions("Fusion 1");
        return fusionModel;
    }
}
