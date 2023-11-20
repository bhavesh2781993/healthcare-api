package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.constants.WorkflowStep;
import com.test.api.healthcare.patients.constants.TrackerStatus;
import com.test.api.healthcare.patients.mappers.PatientTrackerStepMapper;
import com.test.api.healthcare.patients.models.PatientTrackerStepModel;
import com.test.api.healthcare.patients.models.entities.PatientTrackerStep;
import com.test.api.healthcare.patients.services.PatientTrackerStepAggregator;
import com.test.api.healthcare.patients.services.PatientTrackerStepService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientTrackerStepControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_TRACKER_STEP = "/patient-tracker-steps";
    private static final String URI_PATIENT_TRACKER_STEP_ID = URI_PATIENT_TRACKER_STEP + "/{patientTrackerStepId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_TRACKER_STEPS =
        "/patient-trackers/{patientTrackerId}" + URI_PATIENT_TRACKER_STEP;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_TRACKER_STEP_ID = 1L;
    private static final Long WORK_FLOW_STEP_ID = 1L;

    @MockBean
    private PatientTrackerStepMapper patientTrackerStepMapper;

    @MockBean
    private PatientTrackerStepService patientTrackerStepService;

    @MockBean
    private PatientTrackerStepAggregator patientTrackerStepAggregator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientTrackerStep() throws Exception {

        final PatientTrackerStepModel patientTrackerStepModel = getMockedPatientTrackerStepModel(PATIENT_TRACKER_STEP_ID);
        when(patientTrackerStepMapper.toPatientTrackerStepModel(any())).thenReturn(patientTrackerStepModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_TRACKER_STEP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientTrackerStepModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("workflowStepId").description("Workflow Step Id"),
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("status").description("Status of Patient Tracker Step")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getPatientTrackerStep() throws Exception {
        final PatientTrackerStepModel patientTrackerStepModel = getMockedPatientTrackerStepModel(PATIENT_TRACKER_STEP_ID);

        when(patientTrackerStepAggregator.getPatientTrackerStepDetails(anyLong(), anyString()))
            .thenReturn(patientTrackerStepModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_TRACKER_STEPS, PATIENT_TRACKER_ID)
                .queryParam("step", WorkflowStep.PRE_TREATMENT.name())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTrackerId").description("Patient Tracker Id")
                ),
                queryParameters(
                    parameterWithName("step").description("Workflow Step. Allowed values: " + WorkflowStep.getAllowedValues())
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientTrackerStep() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_TRACKER_STEP_ID, PATIENT_TRACKER_STEP_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTrackerStepId").description("Patient Tracker Step Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientTrackerStepModel getMockedPatientTrackerStepModel(final Long patientTrackerStepId) {
        final PatientTrackerStepModel patientTrackerStepModel = new PatientTrackerStepModel();
        patientTrackerStepModel.setPatientTrackerStepId(patientTrackerStepId);
        patientTrackerStepModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientTrackerStepModel.setWorkflowStepId(WORK_FLOW_STEP_ID);
        patientTrackerStepModel.setStatus(TrackerStatus.COMPLETED.name());
        return patientTrackerStepModel;
    }

    private PatientTrackerStep getMockedPatientTrackerStep(final Long patientTrackerStepId) {
        final PatientTrackerStepModel patientTrackerStepModel = getMockedPatientTrackerStepModel(patientTrackerStepId);
        final PatientTrackerStep patientTrackerStep = new PatientTrackerStep();
        patientTrackerStep.setId(patientTrackerStepModel.getPatientTrackerStepId());
        return patientTrackerStep;
    }
}
