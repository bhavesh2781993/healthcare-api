package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientNoteMapper;
import com.test.api.healthcare.patients.models.PatientNoteModel;
import com.test.api.healthcare.patients.models.entities.PatientNote;
import com.test.api.healthcare.patients.services.PatientNoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientNoteControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_NOTE = "/patient-notes";
    private static final String URI_PATIENT_NOTE_ID = URI_PATIENT_NOTE + "/{patientNoteId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_NOTE_ID = URI_PATIENT_NOTE + "/{patientTrackerId}";

    private static final Long PATIENT_NOTE_ID = 1L;
    private static final Long CLINIC_USER_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;

    @MockBean
    private PatientNoteMapper patientNoteMapper;

    @MockBean
    private PatientNoteService patientNoteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientNote() throws Exception {
        final PatientNoteModel patientNoteModel = getMockedPatientNoteModel(PATIENT_NOTE_ID);
        when(patientNoteMapper.toPatientNoteModel(any())).thenReturn(patientNoteModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_NOTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientNoteModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("clinicUserId").description("Clinic User Id"),
                    fieldWithPath("patientTrackerId").description("Patient Tracker ID"),
                    fieldWithPath("patientNote").description("Patient Note")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientNotes() throws Exception {
        final List<PatientNoteModel> patientNoteModels = List.of(getMockedPatientNoteModel(PATIENT_NOTE_ID));
        when(patientNoteMapper.toPatientNoteModelList(any())).thenReturn(patientNoteModels);
        when(patientNoteService.listPatientNotes(any())).thenReturn(paginatedMockedPatientNotes(PATIENT_TRACKER_ID));
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_NOTE_ID, PATIENT_TRACKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParams(getMockedPaginationRequest()))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                queryParameters(
                    parameterWithName("pageNo").description("Page no for which resources are requested. pageNo starts at: 0"),
                    parameterWithName("pageSize").description("No of elements in a page. Max pageSize limit: 100"),
                    parameterWithName("sortField").description("Field on which sorting is requested"),
                    parameterWithName("sortDirection").description("Direction of sort field. Allowed values: [ASC, DESC]")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientNote() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_NOTE_ID, PATIENT_NOTE_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientNoteId").description("Patient Note Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientNoteModel getMockedPatientNoteModel(final Long patientNoteId) {
        final PatientNoteModel patientNoteModel = new PatientNoteModel();
        patientNoteModel.setPatientNoteId(patientNoteId);
        patientNoteModel.setClinicUserId(CLINIC_USER_ID);
        patientNoteModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientNoteModel.setPatientNote("Note 1");
        return patientNoteModel;
    }

    private PatientNote getMockedPatientNote(final Long patientTrackerId) {
        final PatientNoteModel patientNoteModel = getMockedPatientNoteModel(patientTrackerId);
        final PatientNote patientNote = new PatientNote();
        patientNote.setId(patientNoteModel.getPatientTrackerId());
        return patientNote;
    }

    private List<PatientNote> listPatientNoteModels(final Long patientTrackerId) {
        return List.of(getMockedPatientNote(patientTrackerId));
    }

    private Page<PatientNote> paginatedMockedPatientNotes(final Long patientTrackerId) {
        return new PageImpl<>(listPatientNoteModels(patientTrackerId));
    }

    private MultiValueMap<String, String> getMockedPaginationRequest() {
        final LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("pageNo", "0");
        linkedMultiValueMap.add("pageSize", "1");
        linkedMultiValueMap.add("sortField", "name");
        linkedMultiValueMap.add("sortDirection", "asc");
        return linkedMultiValueMap;
    }
}
