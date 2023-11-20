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
import com.test.api.healthcare.configurations.mappers.ImagingAlignmentMapper;
import com.test.api.healthcare.configurations.models.ImagingAlignmentModel;
import com.test.api.healthcare.configurations.services.ImagingAlignmentService;

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
class ImagingAlignmentControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_IMAGING_ALIGNMENT = "/configurations/imaging-alignments";
    private static final String URI_IMAGING_ALIGNMENT_ID = URI_IMAGING_ALIGNMENT + "/{imagingAlignmentId}";
    private static final Long IMAGING_ALIGNMENT_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    ImagingAlignmentService imagingAlignmentService;

    @MockBean
    ImagingAlignmentMapper imagingAlignmentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createImagingAlignment() throws Exception {
        when(imagingAlignmentMapper
            .toImagingAlignmentModel(any()))
            .thenReturn(getMockedImagingAlignmentModel(IMAGING_ALIGNMENT_ID, CLINIC_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_IMAGING_ALIGNMENT)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedImagingAlignmentModel(null, null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                relaxedRequestFields(
                    fieldWithPath("alignTo").description("Imaging Alignment")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);

    }

    @Test
    void listImagingAlignments() throws Exception {
        when(imagingAlignmentMapper
            .toImagingAlignmentModelList(any()))
            .thenReturn(List.of(getMockedImagingAlignmentModel(IMAGING_ALIGNMENT_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_IMAGING_ALIGNMENT)
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
    void deleteImagingAlignment() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_IMAGING_ALIGNMENT_ID, IMAGING_ALIGNMENT_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                pathParameters(parameterWithName("imagingAlignmentId").description("Imaging Alignment Id"))
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeadersSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic Id")
        );
    }

    private ImagingAlignmentModel getMockedImagingAlignmentModel(final Long imagingAlignmentId, final Long clinicId) {
        final ImagingAlignmentModel imagingAlignmentModel = new ImagingAlignmentModel();
        imagingAlignmentModel.setImagingAlignmentId(imagingAlignmentId);
        imagingAlignmentModel.setClinicId(clinicId);
        imagingAlignmentModel.setAlignTo("Imaging Alignment");
        return imagingAlignmentModel;
    }
}
