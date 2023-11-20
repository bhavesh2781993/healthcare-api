package com.test.api.healthcare.configurations.controllers;

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
import com.test.api.healthcare.configurations.mappers.FrequencyMapper;
import com.test.api.healthcare.configurations.models.FrequencyModel;
import com.test.api.healthcare.configurations.services.FrequencyService;

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
class FrequencyControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_FREQUENCY = "/configurations/frequencies";
    private static final String URI_FREQUENCY_ID = URI_FREQUENCY + "/{frequencyId}";
    private static final Long FREQUENCY_ID = 1L;
    private static final Long CLINIC_ID = 1L;


    @MockBean
    private FrequencyMapper frequencyMapper;

    @MockBean
    private FrequencyService frequencyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFrequency() throws Exception {
        final FrequencyModel frequencyModel = getMockedFrequencyModel(FREQUENCY_ID, CLINIC_ID);

        when(frequencyMapper.toFrequencyModel(any())).thenReturn(frequencyModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_FREQUENCY)
                .contentType(APPLICATION_JSON)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .content(objectMapper.writeValueAsString(getMockedFrequencyModel(null, null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                relaxedRequestFields(
                    fieldWithPath("frequency").description("Frequency 1"))
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listFrequency() throws Exception {
        final List<FrequencyModel> frequencyModelList = List.of(getMockedFrequencyModel(FREQUENCY_ID, CLINIC_ID));
        when(frequencyMapper.toFrequencyModelList(any())).thenReturn(frequencyModelList);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_FREQUENCY)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet()
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deleteFrequency() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_FREQUENCY_ID, FREQUENCY_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeadersSnippet(),
                pathParameters(
                    parameterWithName("frequencyId").description("Frequency Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }


    private RequestHeadersSnippet getRequestHeadersSnippet() {
        return requestHeaders(
            headerWithName(HEADER_CLINIC_ID_KEY).description("Clinic Id")
        );
    }

    private FrequencyModel getMockedFrequencyModel(final Long frequencyId, final Long clinicId) {
        final FrequencyModel frequencyModel = new FrequencyModel();
        frequencyModel.setFrequencyId(frequencyId);
        frequencyModel.setClinicId(clinicId);
        frequencyModel.setFrequency("Frequency 1");
        return frequencyModel;
    }
}
