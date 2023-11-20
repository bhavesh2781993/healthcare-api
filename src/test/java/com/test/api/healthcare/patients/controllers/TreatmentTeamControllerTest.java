package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.TreatmentTeamMapper;
import com.test.api.healthcare.patients.models.TreatmentTeamModel;
import com.test.api.healthcare.patients.models.entities.TreatmentTeam;
import com.test.api.healthcare.patients.services.TreatmentTeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class TreatmentTeamControllerTest extends BaseDocumentationTest {

    private static final String URI_TREATMENT_TEAM = "/treatment-teams";
    private static final String URI_TREATMENT_TEAM_ID = URI_TREATMENT_TEAM + "/{treatmentTeamId}";

    private static final Long TREATMENT_TEAM_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long CLINIC_USER_ID = 1L;
    private static final Long CLINIC_DEPARTMENT_ID = 1L;

    @MockBean
    private TreatmentTeamMapper treatmentTeamMapper;

    @MockBean
    private TreatmentTeamService treatmentTeamService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTreatmentTeam() throws Exception {

        final TreatmentTeamModel treatmentTeamModel = getMockedTreatmentTeamModel(TREATMENT_TEAM_ID);
        when(treatmentTeamMapper.toTreatmentTeamModel(any())).thenReturn(treatmentTeamModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_TREATMENT_TEAM)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedTreatmentTeamModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("clinicUserId").description("Clinic User Id"),
                    fieldWithPath("clinicDepartmentId").description("Clinic Department Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updateTreatmentTeam() throws Exception {
        final TreatmentTeamModel treatmentTeamModel = getMockedTreatmentTeamModel(TREATMENT_TEAM_ID);

        when(treatmentTeamMapper.toTreatmentTeamModel(any())).thenReturn(treatmentTeamModel);
        when(treatmentTeamService.updateTreatmentTeam(any())).thenReturn(getMockedTreatmentTeam(TREATMENT_TEAM_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_TREATMENT_TEAM_ID, TREATMENT_TEAM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedTreatmentTeamModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("treatmentTeamId").description("Treatment Team Id")
                ),
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("clinicUserId").description("Clinic User Id"),
                    fieldWithPath("clinicDepartmentId").description("Clinic Department Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getTreatmentTeam() throws Exception {
        final TreatmentTeamModel treatmentTeamModel = getMockedTreatmentTeamModel(TREATMENT_TEAM_ID);

        when(treatmentTeamMapper.toTreatmentTeamModel(any())).thenReturn(treatmentTeamModel);
        when(treatmentTeamService.getTreatmentTeam(any())).thenReturn(Optional.of(getMockedTreatmentTeam(TREATMENT_TEAM_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_TREATMENT_TEAM_ID, TREATMENT_TEAM_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("treatmentTeamId").description("Treatment Team Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listTreatmentTeams() throws Exception {

        final List<TreatmentTeamModel> treatmentTeamModels = List.of(getMockedTreatmentTeamModel(TREATMENT_TEAM_ID));
        final List<TreatmentTeam> treatmentTeamList = List.of(getMockedTreatmentTeam(TREATMENT_TEAM_ID));
        when(treatmentTeamMapper.toTreatmentTeamModelList(any())).thenReturn(treatmentTeamModels);
        when(treatmentTeamService.listTreatmentTeams(any())).thenReturn(treatmentTeamList);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_TREATMENT_TEAM)
                .param("patientTrackerId", String.valueOf(PATIENT_TRACKER_ID))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())

            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientTracker() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_TREATMENT_TEAM_ID, TREATMENT_TEAM_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("treatmentTeamId").description("Treatment Team Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private TreatmentTeamModel getMockedTreatmentTeamModel(final Long treatmentTeamId) {
        final TreatmentTeamModel treatmentTeamModel = new TreatmentTeamModel();
        treatmentTeamModel.setTreatmentTeamId(treatmentTeamId);
        treatmentTeamModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        treatmentTeamModel.setClinicUserId(CLINIC_USER_ID);
        treatmentTeamModel.setClinicDepartmentId(CLINIC_DEPARTMENT_ID);
        return treatmentTeamModel;
    }

    private TreatmentTeam getMockedTreatmentTeam(final Long treatmentTeamId) {
        final TreatmentTeamModel treatmentTeamModel = getMockedTreatmentTeamModel(treatmentTeamId);
        final TreatmentTeam treatmentTeam = new TreatmentTeam();
        treatmentTeam.setId(treatmentTeamModel.getTreatmentTeamId());
        return treatmentTeam;
    }
}
