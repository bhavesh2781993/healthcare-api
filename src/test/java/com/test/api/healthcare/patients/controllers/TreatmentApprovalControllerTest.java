package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.constants.ApprovalFor;
import com.test.api.healthcare.patients.constants.ApprovalStatus;
import com.test.api.healthcare.patients.mappers.TreatmentApprovalMapper;
import com.test.api.healthcare.patients.models.TreatmentApprovalModel;
import com.test.api.healthcare.patients.models.entities.TreatmentApproval;
import com.test.api.healthcare.patients.services.TreatmentApprovalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class TreatmentApprovalControllerTest extends BaseDocumentationTest {

    private static final String URI_TREATMENT_APPROVAL = "/treatment-approvals";
    private static final String URI_TREATMENT_APPROVAL_ID = URI_TREATMENT_APPROVAL + "/{treatmentApprovalId}";
    private static final String URI_PATIENT_TRACKER_TREATMENT_APPROVAL_ID = "/patient-trackers/{patientTrackerId}" + URI_TREATMENT_APPROVAL;

    private static final Long TREATMENT_APPROVAL_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;

    @MockBean
    private TreatmentApprovalMapper treatmentApprovalMapper;

    @MockBean
    private TreatmentApprovalService treatmentApprovalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTreatmentApproval() throws Exception {

        final TreatmentApprovalModel treatmentApprovalModel = getMockedTreatmentApprovalModel(TREATMENT_APPROVAL_ID);

        when(treatmentApprovalMapper.toTreatmentApprovalModel(any())).thenReturn(treatmentApprovalModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_TREATMENT_APPROVAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedTreatmentApprovalModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("approvalFor").description("Approval For. Allowed values :" + ApprovalFor.getAllowedValues()),
                    fieldWithPath("approvalStatus").description("Approval Status. Allowed values :" + ApprovalStatus.getAllowedValues()),
                    fieldWithPath("approvedBy").description("Approved By"),
                    fieldWithPath("approvedOn").description("Approved On")
                )))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getTreatmentApproval() throws Exception {
        final TreatmentApprovalModel treatmentApprovalModel = getMockedTreatmentApprovalModel(TREATMENT_APPROVAL_ID);
        when(treatmentApprovalMapper.toTreatmentApprovalModel(any())).thenReturn(treatmentApprovalModel);
        when(treatmentApprovalService.getTreatmentApproval(any()))
            .thenReturn(Optional.of(getMockedTreatmentApproval(TREATMENT_APPROVAL_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_TREATMENT_APPROVAL_ID, TREATMENT_APPROVAL_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("treatmentApprovalId").description("Treatment Approval Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listTreatmentApprovals() throws Exception {
        final List<TreatmentApprovalModel> treatmentApprovalModels = List.of(getMockedTreatmentApprovalModel(TREATMENT_APPROVAL_ID));
        final List<TreatmentApproval> treatmentApprovals = List.of(getMockedTreatmentApproval(TREATMENT_APPROVAL_ID));

        when(treatmentApprovalMapper.toTreatmentApprovalModelList(any())).thenReturn(treatmentApprovalModels);
        when(treatmentApprovalService.listTreatmentApprovals(any())).thenReturn(treatmentApprovals);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_TREATMENT_APPROVAL_ID, PATIENT_TRACKER_ID)
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
    void deleteTreatmentApproval() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_TREATMENT_APPROVAL_ID, TREATMENT_APPROVAL_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("treatmentApprovalId").description("Treatment Approval Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private TreatmentApprovalModel getMockedTreatmentApprovalModel(final Long treatmentApprovalId) {
        final TreatmentApprovalModel treatmentApprovalModel = new TreatmentApprovalModel();
        treatmentApprovalModel.setTreatmentApprovalId(treatmentApprovalId);
        treatmentApprovalModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        treatmentApprovalModel.setApprovalFor(ApprovalFor.FUSION.name());
        treatmentApprovalModel.setApprovalStatus(ApprovalStatus.COMPLETED.name());
        treatmentApprovalModel.setApprovedBy(1L);
        treatmentApprovalModel.setApprovedOn(LocalDateTime.now());
        return treatmentApprovalModel;
    }

    private TreatmentApproval getMockedTreatmentApproval(final Long treatmentApprovalId) {
        final TreatmentApprovalModel treatmentApprovalModel = getMockedTreatmentApprovalModel(treatmentApprovalId);
        final TreatmentApproval treatmentApproval = new TreatmentApproval();
        treatmentApproval.setId(treatmentApprovalModel.getTreatmentApprovalId());
        return treatmentApproval;
    }
}
