package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.constants.ComparisonOperator;
import com.test.api.healthcare.patients.constants.DoseLimitUnit;
import com.test.api.healthcare.patients.constants.DoseUnit;
import com.test.api.healthcare.patients.constants.StructureMeasure;
import com.test.api.healthcare.patients.mappers.PatientTreatmentGoalMapper;
import com.test.api.healthcare.patients.models.PatientTreatmentGoalModel;
import com.test.api.healthcare.patients.models.entities.PatientTreatmentGoal;
import com.test.api.healthcare.patients.services.PatientTreatmentGoalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientTreatmentGoalControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_TREATMENT_GOAL = "/patient-treatment-goals";
    private static final String URI_PATIENT_TREATMENT_GOAL_ID = URI_PATIENT_TREATMENT_GOAL + "/{patientTreatmentGoalId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_TREATMENT_GOAL_ID =
        "/patient-tracker/{patientTrackerId}" + URI_PATIENT_TREATMENT_GOAL;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_TREATMENT_GOAL_ID = 1L;

    @MockBean
    private PatientTreatmentGoalMapper patientTreatmentGoalMapper;

    @MockBean
    private PatientTreatmentGoalService patientTreatmentGoalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientTreatmentGoal() throws Exception {
        final PatientTreatmentGoalModel patientTreatmentGoalModel = getMockedPatientTreatmentGoalModel(PATIENT_TREATMENT_GOAL_ID);

        when(patientTreatmentGoalMapper.toPatientTreatmentGoalModel(any()))
            .thenReturn(patientTreatmentGoalModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_TREATMENT_GOAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientTreatmentGoalModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("structure").description("Structure"),
                    fieldWithPath("structureMeasure").description("Structure Measure"),
                    fieldWithPath("primaryGoalDose").description("Primary Goal Dose"),
                    fieldWithPath("primaryGoalDoseUnit").description("Primary Goal Dose Unit"),
                    fieldWithPath("primaryGoalDoseComparator").description("Primary Goal Dose Comparator"),
                    fieldWithPath("primaryGoalDoseLimit").description("Primary Goal Dose Limit"),
                    fieldWithPath("primaryGoalDoseLimitUnit").description("Primary Goal Dose Limit Unit"),
                    fieldWithPath("variationDose").description("Variation Dose"),
                    fieldWithPath("variationDoseUnit").description("Variation Dose Unit"),
                    fieldWithPath("variationDoseComparator").description("Variation Dose Comparator"),
                    fieldWithPath("variationDoseLimit").description("Variation Dose Limit"),
                    fieldWithPath("variationDoseLimitUnit").description("Variation Dose Limit Unit"),
                    fieldWithPath("priority").description("Priority"),
                    fieldWithPath("isObjectiveMet").description("Is Objective Met")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientTreatmentGoals() throws Exception {
        final List<PatientTreatmentGoalModel> patientTreatmentGoalModels =
            List.of(getMockedPatientTreatmentGoalModel(PATIENT_TREATMENT_GOAL_ID));

        when(patientTreatmentGoalService.listPatientTreatmentGoals(any()))
            .thenReturn(List.of(getMockedPatientTreatmentGoal(PATIENT_TREATMENT_GOAL_ID)));
        when(patientTreatmentGoalMapper.toPatientTreatmentGoalModelList(anyList()))
            .thenReturn(patientTreatmentGoalModels);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_TREATMENT_GOAL_ID, PATIENT_TRACKER_ID)
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
    void deletePatientTreatmentGoal() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_TREATMENT_GOAL_ID, PATIENT_TREATMENT_GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTreatmentGoalId").description("Patient Treatment Goal Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updatePatientTreatmentGoal() throws Exception {
        final PatientTreatmentGoalModel patientTreatmentGoalModel = getMockedPatientTreatmentGoalModel(PATIENT_TREATMENT_GOAL_ID);

        when(patientTreatmentGoalService.updatePatientTreatmentGoal(any()))
            .thenReturn(getMockedPatientTreatmentGoal(PATIENT_TREATMENT_GOAL_ID));
        when(patientTreatmentGoalMapper.toPatientTreatmentGoalModel(any()))
            .thenReturn(patientTreatmentGoalModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_TREATMENT_GOAL_ID, PATIENT_TREATMENT_GOAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientTreatmentGoalModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTreatmentGoalId").description("Patient Treatment Goal Id")
                ),
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("structure").description("Structure"),
                    fieldWithPath("structureMeasure").description("Structure Measure"),
                    fieldWithPath("primaryGoalDose").description("Primary Goal Dose"),
                    fieldWithPath("primaryGoalDoseUnit").description("Primary Goal Dose Unit"),
                    fieldWithPath("primaryGoalDoseComparator").description("Primary Goal Dose Comparator"),
                    fieldWithPath("primaryGoalDoseLimit").description("Primary Goal Dose Limit"),
                    fieldWithPath("primaryGoalDoseLimitUnit").description("Primary Goal Dose Limit Unit"),
                    fieldWithPath("variationDose").description("Variation Dose"),
                    fieldWithPath("variationDoseUnit").description("Variation Dose Unit"),
                    fieldWithPath("variationDoseComparator").description("Variation Dose Comparator"),
                    fieldWithPath("variationDoseLimit").description("Variation Dose Limit"),
                    fieldWithPath("variationDoseLimitUnit").description("Variation Dose Limit Unit"),
                    fieldWithPath("priority").description("Priority"),
                    fieldWithPath("isObjectiveMet").description("Is Objective Met")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientTreatmentGoalModel getMockedPatientTreatmentGoalModel(final Long patientTreatmentGoalId) {
        final PatientTreatmentGoalModel patientTreatmentGoalModel = new PatientTreatmentGoalModel();
        patientTreatmentGoalModel.setPatientTreatmentGoalId(patientTreatmentGoalId);
        patientTreatmentGoalModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientTreatmentGoalModel.setStructure("Structure 1");
        patientTreatmentGoalModel.setStructureMeasure(StructureMeasure.MEAN_LESS_THAN.name());
        patientTreatmentGoalModel.setPrimaryGoalDose(1);
        patientTreatmentGoalModel.setPrimaryGoalDoseUnit(DoseUnit.PCT_RX.name());
        patientTreatmentGoalModel.setPrimaryGoalDoseComparator(ComparisonOperator.GT.name());
        patientTreatmentGoalModel.setPrimaryGoalDoseLimit(1);
        patientTreatmentGoalModel.setPrimaryGoalDoseLimitUnit(DoseLimitUnit.CGY.name());
        patientTreatmentGoalModel.setVariationDose(1);
        patientTreatmentGoalModel.setVariationDoseUnit(DoseUnit.PCT_RX.name());
        patientTreatmentGoalModel.setVariationDoseComparator(ComparisonOperator.GT.name());
        patientTreatmentGoalModel.setVariationDoseLimit(1);
        patientTreatmentGoalModel.setVariationDoseLimitUnit(DoseLimitUnit.CGY.name());
        patientTreatmentGoalModel.setPriority(1);
        patientTreatmentGoalModel.setIsObjectiveMet(true);
        return patientTreatmentGoalModel;
    }

    private PatientTreatmentGoal getMockedPatientTreatmentGoal(final Long patientTreatmentGoalId) {
        final PatientTreatmentGoalModel patientTreatmentGoalModel = getMockedPatientTreatmentGoalModel(patientTreatmentGoalId);
        final PatientTreatmentGoal patientTreatmentGoal = new PatientTreatmentGoal();
        patientTreatmentGoal.setId(patientTreatmentGoalModel.getPatientTreatmentGoalId());
        return patientTreatmentGoal;
    }
}
