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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientAncillaryCareMapper;
import com.test.api.healthcare.patients.models.PatientAncillaryCareModel;
import com.test.api.healthcare.patients.models.entities.PatientAncillaryCare;
import com.test.api.healthcare.patients.services.PatientAncillaryCareService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientAncillaryCareControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_ANCILLARY_CARES = "/patient-ancillary-cares";
    private static final String URI_PATIENT_ANCILLARY_CARE_ID = URI_PATIENT_ANCILLARY_CARES + "/{patientAncillaryCareId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_ANCILLARY_CARES = "/patient-trackers/{patientTrackerId}"
        + URI_PATIENT_ANCILLARY_CARES;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_ANCILLARY_CARE_ID = 1L;

    @MockBean
    private PatientAncillaryCareMapper patientAncillaryCareMapper;

    @MockBean
    private PatientAncillaryCareService patientAncillaryCareService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createOrUpdatePatientAncillaryCare() throws Exception {
        when(patientAncillaryCareMapper.toPatientAncillaryCareModel(any()))
            .thenReturn(getMockedPatientAncillaryCareModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_ANCILLARY_CARES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientAncillaryCareModel(PATIENT_TRACKER_ID))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("hasTracheotomy").description("Flag for Tracheotomy"),
                    fieldWithPath("hasPegTube").description("Flag for Peg Tube"),
                    fieldWithPath("hasColostomy").description("Flag for Colostomy"),
                    fieldWithPath("isOnSteroid").description("Flag for On Steroid"),
                    fieldWithPath("steroid").description("Steroid"),
                    fieldWithPath("isOnPainMedication").description("Flag for On Pain Medication"),
                    fieldWithPath("painMedication").description("Pain Medication")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getPatientAncillaryCare() throws Exception {
        when(patientAncillaryCareService.getPatientAncillaryCare(anyLong()))
            .thenReturn(Optional.of(getMockedPatientAncillaryCare(PATIENT_TRACKER_ID)));
        when(patientAncillaryCareMapper.toPatientAncillaryCareModel(any()))
            .thenReturn(getMockedPatientAncillaryCareModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_ANCILLARY_CARE_ID, PATIENT_ANCILLARY_CARE_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientAncillaryCareId").description("Patient Ancillary Care Id")
                )
            )).andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void deletePatientAncillaryCare() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_ANCILLARY_CARE_ID, PATIENT_ANCILLARY_CARE_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientAncillaryCareId").description("Patient Ancillary Care Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private PatientAncillaryCareModel getMockedPatientAncillaryCareModel(final Long patientTrackerId) {
        final PatientAncillaryCareModel patientAncillaryCareModel = new PatientAncillaryCareModel();
        patientAncillaryCareModel.setPatientTrackerId(patientTrackerId);
        patientAncillaryCareModel.setHasTracheotomy(false);
        patientAncillaryCareModel.setHasPegTube(true);
        patientAncillaryCareModel.setHasColostomy(false);
        patientAncillaryCareModel.setIsOnSteroid(true);
        patientAncillaryCareModel.setSteroid("Test Steroid");
        patientAncillaryCareModel.setIsOnPainMedication(true);
        patientAncillaryCareModel.setPainMedication("Test Pain Medication");
        return patientAncillaryCareModel;
    }

    private PatientAncillaryCare getMockedPatientAncillaryCare(final Long patientTrackerId) {
        final PatientAncillaryCareModel mockedPatientAncillaryCareModel = getMockedPatientAncillaryCareModel(patientTrackerId);
        final PatientAncillaryCare patientAncillaryCare = new PatientAncillaryCare();
        patientAncillaryCare.setId(mockedPatientAncillaryCareModel.getPatientAncillaryCareId());
        patientAncillaryCare.setHasTracheotomy(false);
        patientAncillaryCare.setHasPegTube(true);
        patientAncillaryCare.setHasColostomy(false);
        patientAncillaryCare.setIsOnSteroid(true);
        patientAncillaryCare.setSteroid("Test Steroid");
        patientAncillaryCare.setIsOnPainMedication(false);
        patientAncillaryCare.setPainMedication("Test Pain Medication");
        return patientAncillaryCare;
    }

}
