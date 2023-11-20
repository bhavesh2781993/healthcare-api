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
import com.test.api.healthcare.configurations.mappers.ImagingTypeMapper;
import com.test.api.healthcare.configurations.models.ImagingTypeModel;
import com.test.api.healthcare.configurations.services.ImagingTypeService;

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
class ImagingTypeTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_IMAGING_TYPE = "/configurations/imaging-types";
    private static final String URI_IMAGING_TYPE_ID = URI_IMAGING_TYPE + "/{imagingTypeId}";
    private static final Long IMAGING_TYPE_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private ImagingTypeMapper imagingTypeMapper;

    @MockBean
    private ImagingTypeService imagingTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createImagingType() throws Exception {
        when(imagingTypeMapper
            .toImagingTypeModel(any()))
            .thenReturn(getMockedImagingTypeModel(IMAGING_TYPE_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_IMAGING_TYPE)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedImagingTypeModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                relaxedRequestFields(
                    fieldWithPath("imagingType").description("Imaging Type")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listImagingTypes() throws Exception {
        when(imagingTypeMapper
            .toImagingTypeModelList(any()))
            .thenReturn(List.of(getMockedImagingTypeModel(IMAGING_TYPE_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_IMAGING_TYPE)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet()
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void deleteImagingType() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_IMAGING_TYPE_ID, IMAGING_TYPE_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                pathParameters(parameterWithName("imagingTypeId").description("Imaging Type Id"))
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeadersSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic Id")
        );
    }



    private ImagingTypeModel getMockedImagingTypeModel(final Long imagingTypeId, final Long clinicId) {
        final ImagingTypeModel imagingTypeModel = new ImagingTypeModel();
        imagingTypeModel.setImagingTypeId(imagingTypeId);
        imagingTypeModel.setClinicId(clinicId);
        imagingTypeModel.setImagingType("Imagine Type");
        return imagingTypeModel;
    }
}

