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
import com.test.api.healthcare.patients.mappers.FollowupPlanMapper;
import com.test.api.healthcare.patients.models.FollowupPlanModel;
import com.test.api.healthcare.patients.models.entities.FollowupPlan;
import com.test.api.healthcare.patients.services.FollowupPlanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class FollowupPlanControllerTest extends BaseDocumentationTest {

    private static final String URI_FOLLOWUP_PLANS = "/followup-plans";
    private static final String URI_FOLLOWUP_PLAN_ID = URI_FOLLOWUP_PLANS + "/{followupPlanId}";
    private static final String URI_PATIENT_TRACKER_FOLLOWUP_PLANS = "/patient-trackers/{patientTrackerId}" + URI_FOLLOWUP_PLANS;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long FOLLOWUP_PLAN_ID = 1L;

    @MockBean
    private FollowupPlanMapper followupPlanMapper;

    @MockBean
    private FollowupPlanService followupPlanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateFollowupPlan() throws Exception {
        when(followupPlanMapper.toFollowupPlanModel(any())).thenReturn(getMockedFollowupPlanModel(PATIENT_TRACKER_ID));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_FOLLOWUP_PLANS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedFollowupPlanModel(PATIENT_TRACKER_ID))))
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
    void getFollowupPlanByPatientTrackerId() throws Exception {
        when(followupPlanService.getFollowupPlanByPatientTrackerId(anyLong()))
            .thenReturn(Optional.of(getMockedFollowupPlan(PATIENT_TRACKER_ID)));
        when(followupPlanMapper.toFollowupPlanModel(any())).thenReturn(getMockedFollowupPlanModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_FOLLOWUP_PLANS, PATIENT_TRACKER_ID)
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
    void deleteFollowupPlan() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_FOLLOWUP_PLAN_ID, FOLLOWUP_PLAN_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("followupPlanId").description("Followup Plan Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private FollowupPlanModel getMockedFollowupPlanModel(final Long patientTrackerId) {
        final FollowupPlanModel followupPlanModel = new FollowupPlanModel();
        followupPlanModel.setPatientTrackerId(patientTrackerId);
        followupPlanModel.setPlanDetail("Test");
        followupPlanModel.setFollowupIn("Test");
        followupPlanModel.setPlan("Test");
        return followupPlanModel;
    }

    private FollowupPlan getMockedFollowupPlan(final Long patientTrackerId) {
        final FollowupPlanModel mockedFollowupPlanModel = getMockedFollowupPlanModel(patientTrackerId);
        final FollowupPlan followupPlan = new FollowupPlan();
        followupPlan.setId(mockedFollowupPlanModel.getPatientTrackerId());
        return followupPlan;
    }

}
