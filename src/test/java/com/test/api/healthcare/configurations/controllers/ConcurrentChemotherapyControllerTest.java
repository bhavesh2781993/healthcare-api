package com.test.api.healthcare.configurations.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.mappers.ConcurrentChemotherapyMapper;
import com.test.api.healthcare.configurations.models.ConcurrentChemotherapyModel;
import com.test.api.healthcare.configurations.services.ConcurrentChemotherapyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class ConcurrentChemotherapyControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_CONCURRENT_CHEMOTHERAPY = "/configurations/concurrent-chemotherapy";
    private static final String URI_CONCURRENT_CHEMOTHERAPY_ID = URI_CONCURRENT_CHEMOTHERAPY + "/{concurrentChemotherapyId}";
    private static final Long CONCURRENT_CHEMOTHERAPY_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private ConcurrentChemotherapyMapper concurrentChemotherapyMapper;

    @MockBean
    private ConcurrentChemotherapyService concurrentChemotherapyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createConcurrentChemotherapy() throws Exception {
        final ConcurrentChemotherapyModel concurrentChemotherapyModel =
            getMockedConcurrentChemotherapyModel(CONCURRENT_CHEMOTHERAPY_ID, CLINIC_ID);
        when(concurrentChemotherapyMapper.toConcurrentChemotherapyModel(any())).thenReturn(concurrentChemotherapyModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_CONCURRENT_CHEMOTHERAPY)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedConcurrentChemotherapyModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                requestFields(
                    fieldWithPath("concurrentChemotherapy").description("Concurrent Chemotherapy")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listConcurrentChemotherapy() throws Exception {
        when(concurrentChemotherapyMapper.toConcurrentChemotherapyModelList(anyList()))
            .thenReturn(List.of(getMockedConcurrentChemotherapyModel(CONCURRENT_CHEMOTHERAPY_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CONCURRENT_CHEMOTHERAPY)
                .contentType(APPLICATION_JSON)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet()
            ))
            .andReturn();

        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteConcurrentChemotherapy() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CONCURRENT_CHEMOTHERAPY_ID, CONCURRENT_CHEMOTHERAPY_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("concurrentChemotherapyId").description("Concurrent Chemotherapy Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private RequestHeadersSnippet getRequestHeaderSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic id")
        );
    }

    private ConcurrentChemotherapyModel getMockedConcurrentChemotherapyModel(final Long concurrentChemotherapyId,
                                                                             final Long clinicId) {
        final ConcurrentChemotherapyModel concurrentChemotherapyModel = new ConcurrentChemotherapyModel();
        concurrentChemotherapyModel.setConcurrentChemotherapyId(concurrentChemotherapyId);
        concurrentChemotherapyModel.setClinicId(clinicId);
        concurrentChemotherapyModel.setConcurrentChemotherapy("CC THERAPY 1");
        return concurrentChemotherapyModel;
    }
}
