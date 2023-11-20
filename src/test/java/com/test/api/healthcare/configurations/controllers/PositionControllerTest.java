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
import com.test.api.healthcare.configurations.constants.PositionType;
import com.test.api.healthcare.configurations.mappers.PositionMapper;
import com.test.api.healthcare.configurations.models.PositionModel;
import com.test.api.healthcare.configurations.services.PositionService;

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
class PositionControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final Long HEADER_CLINIC_ID_VALUE = 1L;
    private static final String URI_CONFIGURATIONS_POSITIONS = "/configurations/positions";
    private static final String URI_CONFIGURATIONS_POSITION_ID = URI_CONFIGURATIONS_POSITIONS + "/{positionId}";
    private static final Long POSITION_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private PositionMapper positionMapper;

    @MockBean
    private PositionService positionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPosition() throws Exception {
        when(positionMapper
            .toPositionModel(any()))
            .thenReturn(getMockedPositionModel(POSITION_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_CONFIGURATIONS_POSITIONS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPositionModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("position").description("Position name"),
                    fieldWithPath("positionType").description("Position type. Allowed values :" + PositionType.getAllowedValues())
                )))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listPositions() throws Exception {
        when(positionMapper
            .toPositionModelList(any()))
            .thenReturn(List.of(getMockedPositionModel(POSITION_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CONFIGURATIONS_POSITIONS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParam("positionType", "ARM")
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                queryParameters(
                    parameterWithName("positionType").description("Position type. Allowed values :" + PositionType.getAllowedValues())
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void deletePosition() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CONFIGURATIONS_POSITION_ID, POSITION_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("positionId").description("Position id")
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

    private PositionModel getMockedPositionModel(final Long positionId, final Long clinicId) {
        final PositionModel positionModel = new PositionModel();
        positionModel.setPositionId(positionId);
        positionModel.setClinicId(clinicId);
        positionModel.setPosition("Arm Over Head");
        positionModel.setPositionType("ARM");
        return positionModel;
    }
}
