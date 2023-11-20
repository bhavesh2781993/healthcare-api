package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientChartMapper;
import com.test.api.healthcare.patients.models.PatientChartModel;
import com.test.api.healthcare.patients.models.entities.PatientChart;
import com.test.api.healthcare.patients.services.PatientChartService;

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
class PatientChartControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_CHART = "/patient-charts";
    private static final String URI_PATIENT_CHART_ID = URI_PATIENT_CHART + "/{patientChartId}";
    private static final String URI_PATIENT_TRACKER_ID = "/patient-trackers/{patientTrackerId}" + URI_PATIENT_CHART;

    private static final Long PATIENT_CHART_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;

    @MockBean
    private PatientChartMapper patientChartMapper;

    @MockBean
    private PatientChartService patientChartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updatePatientChart() throws Exception {
        final PatientChartModel patientChartModel = getMockedPatientChartModel(PATIENT_CHART_ID);

        when(patientChartMapper.toPatientChartModel(any())).thenReturn(patientChartModel);
        when(patientChartService.updatePatientChart(any(), any())).thenReturn(getMockedPatientChart(PATIENT_TRACKER_ID));
        when(patientChartMapper.toPatientChart(any())).thenReturn(getMockedPatientChart(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .patch(URI_PATIENT_CHART_ID, PATIENT_CHART_ID)
                .content(objectMapper.writeValueAsString(getMockedPatientChartModel(null)))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientChartId").description("Patient Chart Id")
                ),
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("isReviewed").description("Is Reviewed")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getPatientChart() throws Exception {
        final PatientChartModel patientChartModel = getMockedPatientChartModel(PATIENT_TRACKER_ID);
        when(patientChartService.getPatientChart(any())).thenReturn(Optional.of(getMockedPatientChart(PATIENT_TRACKER_ID)));
        when(patientChartMapper.toPatientChartModel(any())).thenReturn(patientChartModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_ID, PATIENT_TRACKER_ID)
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
    void deletePatientChart() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_CHART_ID, PATIENT_CHART_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientChartId").description("Patient Chart Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientChartModel getMockedPatientChartModel(final Long patientTrackerId) {
        final PatientChartModel patientChartModel = new PatientChartModel();
        patientChartModel.setPatientChartId(patientTrackerId);
        patientChartModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientChartModel.setNote("test 1");
        patientChartModel.setIsReviewed(true);
        return patientChartModel;
    }

    private PatientChart getMockedPatientChart(final Long patientTrackerId) {
        final PatientChartModel patientChartModel = getMockedPatientChartModel(patientTrackerId);
        final PatientChart patientChart = new PatientChart();
        patientChart.setId(patientChartModel.getPatientChartId());
        return patientChart;
    }
}
