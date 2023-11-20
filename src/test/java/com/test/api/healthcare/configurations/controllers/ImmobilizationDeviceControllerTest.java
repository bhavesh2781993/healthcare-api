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
import com.test.api.healthcare.configurations.mappers.ImmobilizationDeviceMapper;
import com.test.api.healthcare.configurations.models.ImmobilizationDeviceModel;
import com.test.api.healthcare.configurations.services.ImmobilizationDeviceService;

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
class ImmobilizationDeviceControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final String HEADER_CLINIC_ID_VALUE = "1";
    private static final String URI_IMMOBILIZATION_DEVICE = "/configurations/immobilization-devices";
    private static final String URI_IMMOBILIZATION_DEVICE_ID = URI_IMMOBILIZATION_DEVICE + "/{immobilizationDeviceId}";
    private static final Long IMMOBILIZATION_DEVICE_ID = 1L;
    private static final Long CLINIC_ID = 1L;

    @MockBean
    private ImmobilizationDeviceMapper immobilizationDeviceMapper;

    @MockBean
    private ImmobilizationDeviceService immobilizationDeviceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createImmobilizationDevice()throws Exception {
        when(immobilizationDeviceMapper
            .toImmobilizationDeviceModel(any()))
            .thenReturn(getMockedImmobilizationDeviceModel(IMMOBILIZATION_DEVICE_ID, CLINIC_ID));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_IMMOBILIZATION_DEVICE)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedImmobilizationDeviceModel(null, null))))
                .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("device").description("Immobilization Device")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void listImmobilizationDevices() throws Exception {
        when(immobilizationDeviceMapper
            .toImmobilizationDeviceModelList(any()))
            .thenReturn(List.of(getMockedImmobilizationDeviceModel(IMMOBILIZATION_DEVICE_ID, CLINIC_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_IMMOBILIZATION_DEVICE)
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
    void deleteImmobilizationDevice() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
            .delete(URI_IMMOBILIZATION_DEVICE_ID, IMMOBILIZATION_DEVICE_ID)
            .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                pathParameters(
                    parameterWithName("immobilizationDeviceId").description("Immobilization Device Id")
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

    private ImmobilizationDeviceModel getMockedImmobilizationDeviceModel(final Long immobilizationDeviceId, final Long clinicId) {
        final ImmobilizationDeviceModel immobilizationDeviceModel = new ImmobilizationDeviceModel();
        immobilizationDeviceModel.setImmobilizationDeviceId(immobilizationDeviceId);
        immobilizationDeviceModel.setClinicId(clinicId);
        immobilizationDeviceModel.setDevice("Device 1");
        return immobilizationDeviceModel;
    }
}
