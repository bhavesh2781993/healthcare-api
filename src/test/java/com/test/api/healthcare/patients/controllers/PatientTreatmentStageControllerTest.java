package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientTreatmentStageMapper;
import com.test.api.healthcare.patients.models.PatientTreatmentStageModel;
import com.test.api.healthcare.patients.models.entities.PatientTreatmentStage;
import com.test.api.healthcare.patients.services.PatientTreatmentStageService;

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
class PatientTreatmentStageControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_TREATMENT_STAGE = "/patient-treatment-stages";
    private static final String URI_PATIENT_TRACKER_PATIENT_TREATMENT_STAGE_ID =
        "/patient-trackers/{patientTrackerId}" + URI_PATIENT_TREATMENT_STAGE;
    private static final String URI_PATIENT_TREATMENT_STAGE_ID = URI_PATIENT_TREATMENT_STAGE + "/{patientTreatmentStageId}";

    private static final Long PATIENT_TREATMENT_STAGE_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long TREATMENT_STAGE_ID = 1L;

    @MockBean
    private PatientTreatmentStageMapper patientTreatmentStageMapper;

    @MockBean
    private PatientTreatmentStageService patientTreatmentStageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void bulkCreatePatientTreatmentStage() throws Exception {
        final List<PatientTreatmentStageModel> listPatientTreatmentStageModel = listPatientTreatmentStageModel(PATIENT_TREATMENT_STAGE_ID);

        when(patientTreatmentStageMapper.toPatientTreatmentStageModelList(any())).thenReturn(listPatientTreatmentStageModel);

        final PatientTreatmentStageModel mockedPatientTreatmentStageModel = getMockedPatientTreatmentStageModel(null);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_TRACKER_PATIENT_TREATMENT_STAGE_ID, PATIENT_TRACKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(mockedPatientTreatmentStageModel))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(parameterWithName("patientTrackerId").description("Patient Tracker Id")),
                relaxedRequestFields(
                    fieldWithPath("[]").description("Sim Fusion Array"),
                    fieldWithPath("[].patientTreatmentStageId").description("Patient Treatment Stage Id").type(Long.class).optional(),
                    fieldWithPath("[].treatmentStageId").description("Treatment Stage Id"),
                    fieldWithPath("[].spreadType").description("Spread Type"),
                    fieldWithPath("[].stageType").description("Stage Type")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void bulkUpdatePatientTreatmentStage() throws Exception {
        final List<PatientTreatmentStageModel> listPatientTreatmentStageModel = listPatientTreatmentStageModel(PATIENT_TRACKER_ID);
        when(patientTreatmentStageMapper.toPatientTreatmentStageModelList(any())).thenReturn(listPatientTreatmentStageModel);
        final PatientTreatmentStageModel mockedPatientTreatmentStageModel = getMockedPatientTreatmentStageModel(null);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_TRACKER_PATIENT_TREATMENT_STAGE_ID, PATIENT_TRACKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(mockedPatientTreatmentStageModel))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(parameterWithName("patientTrackerId").description("Patient Tracker Id")),
                relaxedRequestFields(
                    fieldWithPath("[]").description("Sim Fusion Array"),
                    fieldWithPath("[].patientTreatmentStageId").description("Patient Treatment Stage Id").type(Long.class).optional(),
                    fieldWithPath("[].treatmentStageId").description("Treatment Stage Id"),
                    fieldWithPath("[].spreadType").description("Spread Type"),
                    fieldWithPath("[].stageType").description("Stage Type")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientTreatmentStages() throws Exception {
        final List<PatientTreatmentStageModel> listPatientTreatmentStageModel = listPatientTreatmentStageModel(PATIENT_TRACKER_ID);
        final List<PatientTreatmentStage> patientTreatmentStages = List.of(getMockedPatientTreatmentStage(PATIENT_TRACKER_ID));

        when(patientTreatmentStageMapper.toPatientTreatmentStageModelList(any())).thenReturn(listPatientTreatmentStageModel);
        when(patientTreatmentStageService.listPatientTreatmentStages(any())).thenReturn(patientTreatmentStages);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_TREATMENT_STAGE_ID, PATIENT_TRACKER_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(parameterWithName("patientTrackerId").description("Patient Tracker Id"))
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientTreatmentStage() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_TREATMENT_STAGE_ID, PATIENT_TREATMENT_STAGE_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(parameterWithName("patientTreatmentStageId").description("Patient Treatment Stage Id"))
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientTreatmentStageModel getMockedPatientTreatmentStageModel(final Long patientTreatmentStageId) {
        final PatientTreatmentStageModel patientTreatmentStageModel = new PatientTreatmentStageModel();
        patientTreatmentStageModel.setPatientTreatmentStageId(patientTreatmentStageId);
        patientTreatmentStageModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientTreatmentStageModel.setTreatmentStageId(TREATMENT_STAGE_ID);
        patientTreatmentStageModel.setStageType("Clinical");
        patientTreatmentStageModel.setSpreadType("Tumor");
        return patientTreatmentStageModel;
    }

    private PatientTreatmentStage getMockedPatientTreatmentStage(final Long patientTreatmentStageId) {
        final PatientTreatmentStageModel patientTreatmentStageModel = getMockedPatientTreatmentStageModel(patientTreatmentStageId);
        final PatientTreatmentStage patientTreatmentStage = new PatientTreatmentStage();
        patientTreatmentStage.setId(patientTreatmentStageModel.getTreatmentStageId());
        return patientTreatmentStage;
    }

    private List<PatientTreatmentStageModel> listPatientTreatmentStageModel(final Long patientTreatmentStageId) {
        return List.of(getMockedPatientTreatmentStageModel(patientTreatmentStageId));
    }
}
