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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.OtvNoteMapper;
import com.test.api.healthcare.patients.models.OtvNoteModel;
import com.test.api.healthcare.patients.models.entities.OtvNote;
import com.test.api.healthcare.patients.services.OtvNoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class OtvNoteControllerTest extends BaseDocumentationTest {

    private static final String URI_OTV_NOTES = "/otv-notes";
    private static final String URI_OTV_NOTE_ID = URI_OTV_NOTES + "/{otvNoteId}";
    private static final String URI_PATIENT_TRACKER_OTV_NOTES = "/patient-trackers/{patientTrackerId}" + URI_OTV_NOTES;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long OTV_NOTE_ID = 1L;

    @MockBean
    private OtvNoteMapper otvNoteMapper;

    @MockBean
    private OtvNoteService otvNoteService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createOtvNote() throws Exception {
        when(otvNoteMapper.toOtvNoteModel(any())).thenReturn(getMockedOtvNoteModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_OTV_NOTES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedOtvNoteModel(PATIENT_TRACKER_ID))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void updateOtvNote() throws Exception {
        when(otvNoteService.getOtvNote(anyLong())).thenReturn(Optional.of(getMockedOtvNote(PATIENT_TRACKER_ID)));
        when(otvNoteMapper.toOtvNote(any())).thenReturn(getMockedOtvNote(PATIENT_TRACKER_ID));
        when(otvNoteMapper.toOtvNoteModel(any())).thenReturn(getMockedOtvNoteModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_OTV_NOTE_ID, OTV_NOTE_ID)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedOtvNoteModel(PATIENT_TRACKER_ID))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("otvNoteId").description("Otv NoteId")
                ),
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getOtvNote() throws Exception {
        when(otvNoteService.getOtvNote(anyLong())).thenReturn(Optional.of(getMockedOtvNote(PATIENT_TRACKER_ID)));
        when(otvNoteMapper.toOtvNoteModel(any())).thenReturn(getMockedOtvNoteModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_OTV_NOTE_ID, OTV_NOTE_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("otvNoteId").description("Otv NoteId")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listOtvNotes() throws Exception {
        when(otvNoteService.listOtvNotes(any())).thenReturn(paginatedMockedOtvNotes(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_OTV_NOTES, PATIENT_TRACKER_ID)
                .queryParams(getMockedPaginationRequest())
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTrackerId").description("Patient Tracker Id")
                ),
                queryParameters(
                    parameterWithName("pageNo").description("Page no for which resources are requested. pageNo starts at: 0"),
                    parameterWithName("pageSize").description("No of elements in a page. Max pageSize limit: 100"),
                    parameterWithName("sortField").description("Field on which sorting is requested"),
                    parameterWithName("sortDirection").description("Direction of sort field. Allowed values: [ASC, DESC]")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void deleteOtvNote() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_OTV_NOTE_ID, OTV_NOTE_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("otvNoteId").description("Otv NoteId")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private OtvNoteModel getMockedOtvNoteModel(final Long patientTrackerId) {
        final OtvNoteModel otvNoteModel = new OtvNoteModel();
        otvNoteModel.setPatientTrackerId(patientTrackerId);
        return otvNoteModel;
    }

    private OtvNote getMockedOtvNote(final Long patientTrackerId) {
        final OtvNoteModel mockedOtvNoteModel = getMockedOtvNoteModel(patientTrackerId);
        final OtvNote otvNote = new OtvNote();
        otvNote.setId(mockedOtvNoteModel.getOtvNoteId());
        return otvNote;
    }

    private List<OtvNote> listMockedOtvNotes(final Long patientTrackerId) {
        return List.of(getMockedOtvNote(patientTrackerId));
    }

    private Page<OtvNote> paginatedMockedOtvNotes(final Long patientTrackerId) {
        return new PageImpl<>(listMockedOtvNotes(patientTrackerId));
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
