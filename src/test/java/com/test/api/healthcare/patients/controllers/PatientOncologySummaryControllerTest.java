package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientOncologySummaryMapper;
import com.test.api.healthcare.patients.models.PatientOncologySummaryModel;
import com.test.api.healthcare.patients.models.entities.PatientOncologySummary;
import com.test.api.healthcare.patients.services.PatientOncologySummaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientOncologySummaryControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_ONCOLOGY_SUMMARY = "/patient-oncology-summary";
    private static final String URI_PATIENT_ONCOLOGY_SUMMARY_ID = URI_PATIENT_ONCOLOGY_SUMMARY + "/{patientOncologySummaryId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_ONCOLOGY_SUMMARY_ID =
        "/patient-trackers/{patientTrackerId}" + URI_PATIENT_ONCOLOGY_SUMMARY;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_ONCOLOGY_SUMMARY_ID = 1L;

    @MockBean
    private PatientOncologySummaryMapper patientOncologySummaryMapper;

    @MockBean
    private PatientOncologySummaryService patientOncologySummaryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdatePatientOncologySummary() throws Exception {
        final PatientOncologySummaryModel patientOncologySummaryModel = getMockedPatientOncologySummaryModel(PATIENT_TRACKER_ID);
        when(patientOncologySummaryMapper.toPatientOncologySummaryModel(any())).thenReturn(patientOncologySummaryModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_ONCOLOGY_SUMMARY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(getMockedPatientOncologySummaryModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker ID"),
                    fieldWithPath("diagnosis").description("Diagnosis"),
                    fieldWithPath("briefPlan").description("Brief Plan"),
                    fieldWithPath("summary").description("Summary")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getPatientOncologySummary() throws Exception {
        final PatientOncologySummaryModel patientOncologySummaryModel = getMockedPatientOncologySummaryModel(PATIENT_TRACKER_ID);
        when(patientOncologySummaryMapper.toPatientOncologySummaryModel(any())).thenReturn(patientOncologySummaryModel);
        when(patientOncologySummaryService.getPatientOncologySummary(any()))
            .thenReturn(Optional.of(getMockedPatientOncologySummary(PATIENT_TRACKER_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_ONCOLOGY_SUMMARY_ID, PATIENT_TRACKER_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTrackerId").description("Patient Tracker Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientOncologySummary() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_ONCOLOGY_SUMMARY_ID, PATIENT_ONCOLOGY_SUMMARY_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientOncologySummaryId").description("Patient Oncology Summary Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientOncologySummaryModel getMockedPatientOncologySummaryModel(final Long patientTrackerId) {
        final PatientOncologySummaryModel patientOncologySummaryModel = new PatientOncologySummaryModel();
        patientOncologySummaryModel.setPatientOncologySummaryId(patientTrackerId);
        patientOncologySummaryModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientOncologySummaryModel.setSummary("Summary 1");
        patientOncologySummaryModel.setDiagnosis("Diagnosis 1");
        patientOncologySummaryModel.setBriefPlan("Test Planing");
        return patientOncologySummaryModel;
    }

    private PatientOncologySummary getMockedPatientOncologySummary(final Long patientTrackerId) {
        final PatientOncologySummaryModel patientOncologySummaryModel = getMockedPatientOncologySummaryModel(patientTrackerId);
        final PatientOncologySummary patientOncologySummary = new PatientOncologySummary();
        patientOncologySummary.setId(patientOncologySummaryModel.getPatientOncologySummaryId());
        return patientOncologySummary;
    }
}
