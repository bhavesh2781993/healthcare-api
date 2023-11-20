package com.test.api.healthcare.configurations.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.test.api.healthcare.configurations.mappers.ClinicLocationMapper;
import com.test.api.healthcare.configurations.models.ClinicLocationModel;
import com.test.api.healthcare.configurations.models.entities.ClinicLocation;
import com.test.api.healthcare.configurations.services.ClinicLocationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class ClinicLocationControllerTest extends BaseDocumentationTest {

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final Long HEADER_CLINIC_ID_VALUE = 1L;
    private static final String URI_LOCATIONS = "/locations";
    private static final String URI_CLINIC_LOCATIONS_ID = URI_LOCATIONS + "/{clinicLocationId}";
    private static final Long CLINIC_LOCATION_ID = 1L;
    private static final String CLINIC_LOCATION_DATA = "Test Clinic Location";

    @MockBean
    private ClinicLocationMapper clinicLocationMapper;

    @MockBean
    private ClinicLocationService clinicLocationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClinicLocation() throws Exception {
        when(clinicLocationMapper.toClinicLocationModel(any())).thenReturn(getMockedClinicLocationModel(CLINIC_LOCATION_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_LOCATIONS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedClinicLocationModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                relaxedRequestFields(
                    fieldWithPath("street").description("Street name"),
                    fieldWithPath("city").description("City name"),
                    fieldWithPath("state").description("State name"),
                    fieldWithPath("timezone").description("timezone")
                )
            ))
            .andReturn();
        assertNotNull(mvcResult);
    }

    @Test
    void getClinicLocation() throws Exception {
        when(clinicLocationService.getClinicLocation(anyLong())).thenReturn(Optional.of(getMockedClinicLocation(CLINIC_LOCATION_ID)));
        when(clinicLocationMapper.toClinicLocationModel(any())).thenReturn(getMockedClinicLocationModel(CLINIC_LOCATION_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_CLINIC_LOCATIONS_ID, CLINIC_LOCATION_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicLocationId").description("Clinic Location Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listClinicLocations() throws Exception {
        when(clinicLocationService.listClinicLocations(any())).thenReturn(paginatedMockedClinicLocations(CLINIC_LOCATION_ID));
        when(clinicLocationMapper.toClinicLocationModelList(any())).thenReturn(List.of(getMockedClinicLocationModel(CLINIC_LOCATION_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_LOCATIONS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParams(getMockedPaginationRequest())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                getRequestHeaderSnippet(),
                queryParameters(
                    parameterWithName("pageNo").description("Page no for which resources are requested. pageNo starts at: 0"),
                    parameterWithName("pageSize").description("No of elements in a page. Max pageSize limit: 100"),
                    parameterWithName("sortField").description("Field on which sorting is requested"),
                    parameterWithName("sortDirection").description("Direction of sort field. Allowed values: [ASC, DESC]")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void deleteClinicLocation() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_CLINIC_LOCATIONS_ID, CLINIC_LOCATION_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("clinicLocationId").description("Clinic Location Id")
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

    private ClinicLocation getMockedClinicLocation(final Long clinicLocationId) {
        final ClinicLocationModel mockedClinicLocationModel = getMockedClinicLocationModel(clinicLocationId);
        final ClinicLocation clinicLocation = new ClinicLocation();
        clinicLocation.setId(mockedClinicLocationModel.getClinicLocationId());
        return clinicLocation;
    }

    private ClinicLocationModel getMockedClinicLocationModel(final Long clinicLocationId) {
        final ClinicLocationModel clinicLocationModel = new ClinicLocationModel();
        clinicLocationModel.setClinicLocationId(clinicLocationId);
        clinicLocationModel.setStreet(CLINIC_LOCATION_DATA);
        clinicLocationModel.setCity(CLINIC_LOCATION_DATA);
        clinicLocationModel.setState(CLINIC_LOCATION_DATA);
        clinicLocationModel.setTimezone(CLINIC_LOCATION_DATA);
        return clinicLocationModel;
    }

    private Page<ClinicLocation> paginatedMockedClinicLocations(final Long clinicLocationId) {
        return new PageImpl<>(List.of(getMockedClinicLocation(clinicLocationId)));
    }

    private MultiValueMap<String, String> getMockedPaginationRequest() {
        final LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("pageNo", "0");
        linkedMultiValueMap.add("pageSize", "1");
        linkedMultiValueMap.add("sortField", "name");
        linkedMultiValueMap.add("sortDirection", "asc");
        return linkedMultiValueMap;
    }

}

