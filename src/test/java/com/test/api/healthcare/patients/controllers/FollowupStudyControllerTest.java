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
import com.test.api.healthcare.patients.mappers.FollowupStudyMapper;
import com.test.api.healthcare.patients.models.FollowupStudyModel;
import com.test.api.healthcare.patients.models.entities.FollowupStudy;
import com.test.api.healthcare.patients.services.FollowupStudyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class FollowupStudyControllerTest extends BaseDocumentationTest {

    private static final String URI_FOLLOWUP_STUDIES = "/followup-studies";
    private static final String URI_FOLLOWUP_STUDY_ID = URI_FOLLOWUP_STUDIES + "/{followupStudyId}";
    private static final String URI_PATIENT_TRACKER_FOLLOWUP_STUDIES = "/patient-trackers/{patientTrackerId}" + URI_FOLLOWUP_STUDIES;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long FOLLOWUP_STUDY_ID = 1L;

    @MockBean
    private FollowupStudyMapper followupStudyMapper;

    @MockBean
    private FollowupStudyService followupStudyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateFollowupStudy() throws Exception {
        when(followupStudyMapper.toFollowupStudyModel(any())).thenReturn(getMockedFollowupStudyModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_FOLLOWUP_STUDIES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedFollowupStudyModel(PATIENT_TRACKER_ID))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker ID")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getFollowupStudyByPatientTrackerId() throws Exception {
        when(followupStudyService.getFollowupStudyByPatientTrackerId(anyLong()))
            .thenReturn(Optional.of(getMockedFollowupStudy(PATIENT_TRACKER_ID)));
        when(followupStudyMapper.toFollowupStudyModel(any())).thenReturn(getMockedFollowupStudyModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_FOLLOWUP_STUDIES, PATIENT_TRACKER_ID)
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
    void deleteFollowupStudy() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_FOLLOWUP_STUDY_ID, FOLLOWUP_STUDY_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("followupStudyId").description("Followup Study Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private FollowupStudyModel getMockedFollowupStudyModel(final Long patientTrackerId) {
        final FollowupStudyModel followupStudyModel = new FollowupStudyModel();
        followupStudyModel.setPatientTrackerId(patientTrackerId);
        return followupStudyModel;
    }

    private FollowupStudy getMockedFollowupStudy(final Long patientTrackerId) {
        final FollowupStudyModel mockedFollowupStudyModel = getMockedFollowupStudyModel(patientTrackerId);
        final FollowupStudy followupStudy = new FollowupStudy();
        followupStudy.setId(mockedFollowupStudyModel.getPatientTrackerId());
        return followupStudy;
    }

}
