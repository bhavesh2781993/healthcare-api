package com.test.api.healthcare.patients.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.constants.TrackerStatus;
import com.test.api.healthcare.patients.mappers.PatientTrackerMapper;
import com.test.api.healthcare.patients.models.PatientTrackerModel;
import com.test.api.healthcare.patients.models.entities.PatientTracker;
import com.test.api.healthcare.patients.services.PatientTrackerService;
import com.test.api.healthcare.patients.services.helper.PatientTrackerHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientTrackerControllerTest extends BaseDocumentationTest {
    private static final String URI_PATIENT_TRACKERS = "/patient-trackers";
    private static final String URI_PATIENT_TRACKER_ID = URI_PATIENT_TRACKERS + "/{id}";

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final Long HEADER_CLINIC_ID_VALUE = 1L;

    private static final Long PATIENT_TRACKER_ID = 1L;

    @MockBean
    private PatientTrackerMapper patientTrackerMapper;

    @MockBean
    private PatientTrackerService patientTrackerService;

    @MockBean
    private PatientTrackerHelper patientTrackerHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientTracker() throws Exception {
        when(patientTrackerMapper.toPatientTrackerModel(any())).thenReturn(getMockedPatientTrackerModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_TRACKERS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientTrackerModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientId").description("Patient Id"),
                    fieldWithPath("clinicWorkflowId").description("Clinic Workflow Id"),
                    fieldWithPath("status").description("Status of Patient Tracker")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getPatientTracker() throws Exception {
        when(patientTrackerService.getPatientTracker(anyLong())).thenReturn(Optional.of(getMockedPatientTracker(PATIENT_TRACKER_ID)));
        when(patientTrackerMapper.toPatientTrackerModel(any())).thenReturn(getMockedPatientTrackerModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_ID, PATIENT_TRACKER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("id").description("Patient Tracker id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listPatientTrackers() throws Exception {
        when(patientTrackerService.listPatientTrackers(any())).thenReturn(paginatedMockedPatientTrackers(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKERS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParams(getMockedPaginationRequest())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
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
    void deletePatientTracker() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_TRACKER_ID, PATIENT_TRACKER_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("id").description("Patient Tracker id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private PatientTrackerModel getMockedPatientTrackerModel(final Long patientTrackerId) {
        final PatientTrackerModel patientTrackerModel = new PatientTrackerModel();
        patientTrackerModel.setPatientTrackerId(patientTrackerId);
        patientTrackerModel.setPatientId(1L);
        patientTrackerModel.setClinicWorkflowId(1L);
        patientTrackerModel.setStatus(TrackerStatus.RUNNING.name());
        return patientTrackerModel;
    }

    private PatientTracker getMockedPatientTracker(final Long patientTrackerId) {
        final PatientTrackerModel mockedPatientTrackerModel = getMockedPatientTrackerModel(patientTrackerId);
        final PatientTracker patientTracker = new PatientTracker();
        patientTracker.setId(mockedPatientTrackerModel.getPatientTrackerId());
        return patientTracker;
    }

    private List<PatientTracker> listMockedPatientTrackers(final Long patientTrackerId) {
        return List.of(getMockedPatientTracker(patientTrackerId));
    }

    private Page<PatientTracker> paginatedMockedPatientTrackers(final Long patientTrackerId) {
        return new PageImpl<>(listMockedPatientTrackers(patientTrackerId));
    }

    private MultiValueMap<String, String> getMockedPaginationRequest() {
        final LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("pageNo", "0");
        linkedMultiValueMap.add("pageSize", "1");
        linkedMultiValueMap.add("sortField", "status");
        linkedMultiValueMap.add("sortDirection", "asc");
        return linkedMultiValueMap;
    }

}
