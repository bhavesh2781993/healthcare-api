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
import com.test.api.healthcare.patients.mappers.OnTreatmentIssueMapper;
import com.test.api.healthcare.patients.models.OnTreatmentIssueModel;
import com.test.api.healthcare.patients.models.entities.OnTreatmentIssue;
import com.test.api.healthcare.patients.services.OnTreatmentIssueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class OnTreatmentIssueControllerTest extends BaseDocumentationTest {

    private static final String URI_ON_TREATMENT_ISSUES = "/on-treatment-issues";
    private static final String URI_ON_TREATMENT_ISSUE_ID = URI_ON_TREATMENT_ISSUES + "/{onTreatmentIssueId}";
    private static final String URI_PATIENT_TRACKER_ON_TREATMENT_ISSUES = "/patient-trackers/{patientTrackerId}" + URI_ON_TREATMENT_ISSUES;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long ON_TREATMENT_ISSUE_ID = 1L;

    @MockBean
    private OnTreatmentIssueMapper onTreatmentIssueMapper;

    @MockBean
    private OnTreatmentIssueService onTreatmentIssueService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateOnTreatmentIssue() throws Exception {
        when(onTreatmentIssueMapper.toOnTreatmentIssueModel(any())).thenReturn(getMockedOnTreatmentIssueModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_ON_TREATMENT_ISSUES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedOnTreatmentIssueModel(PATIENT_TRACKER_ID))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker ID"),
                    fieldWithPath("treatmentIssue").description("Treatment issue")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getOnTreatmentIssue() throws Exception {
        when(onTreatmentIssueService.getOnTreatmentIssue(anyLong()))
            .thenReturn(Optional.of(getMockedOnTreatmentIssue(PATIENT_TRACKER_ID)));
        when(onTreatmentIssueMapper.toOnTreatmentIssueModel(any())).thenReturn(getMockedOnTreatmentIssueModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_ON_TREATMENT_ISSUES, PATIENT_TRACKER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTrackerId").description("Patient Tracker Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void deleteOnTreatmentIssue() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_ON_TREATMENT_ISSUE_ID, ON_TREATMENT_ISSUE_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("onTreatmentIssueId").description("On Treatment Issue Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private OnTreatmentIssueModel getMockedOnTreatmentIssueModel(final Long patientTrackerId) {
        final OnTreatmentIssueModel onTreatmentIssueModel = new OnTreatmentIssueModel();
        onTreatmentIssueModel.setPatientTrackerId(patientTrackerId);
        onTreatmentIssueModel.setTreatmentIssue("Test");
        return onTreatmentIssueModel;
    }

    private OnTreatmentIssue getMockedOnTreatmentIssue(final Long patientTrackerId) {
        final OnTreatmentIssueModel mockedOnTreatmentIssueModel = getMockedOnTreatmentIssueModel(patientTrackerId);
        final OnTreatmentIssue onTreatmentIssue = new OnTreatmentIssue();
        onTreatmentIssue.setId(mockedOnTreatmentIssueModel.getPatientTrackerId());
        return onTreatmentIssue;
    }

}
