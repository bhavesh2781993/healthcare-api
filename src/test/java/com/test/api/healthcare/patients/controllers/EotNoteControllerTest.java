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
import com.test.api.healthcare.patients.mappers.EotNoteMapper;
import com.test.api.healthcare.patients.models.EotNoteModel;
import com.test.api.healthcare.patients.models.entities.EotNote;
import com.test.api.healthcare.patients.services.EotNoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class EotNoteControllerTest extends BaseDocumentationTest {

    private static final String URI_EOT_NOTES = "/eot-notes";
    private static final String URI_PATIENT_TRACKER_EOT_NOTES = "/patient-trackers/{patientTrackerId}" + URI_EOT_NOTES;

    private static final Long PATIENT_TRACKER_ID = 1L;

    @MockBean
    private EotNoteMapper eotNoteMapper;

    @MockBean
    private EotNoteService eotNoteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateEotNote() throws Exception {
        when(eotNoteMapper.toEotNoteModel(any())).thenReturn(getMockedEotNoteModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_EOT_NOTES)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedEotNoteModel(PATIENT_TRACKER_ID))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker ID"),
                    fieldWithPath("note").description("End of Treatment Note"),
                    fieldWithPath("isEarlyEot").description("Flag to indicate if in case of early End of Treatment. Defaults to False"),
                    fieldWithPath("status").description("End of Treatment Note status. Defaults to INCOMPLETE")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getEotNote() throws Exception {
        when(eotNoteService.getEotNote(anyLong())).thenReturn(Optional.of(getMockedEotNote(PATIENT_TRACKER_ID)));
        when(eotNoteMapper.toEotNoteModel(any())).thenReturn(getMockedEotNoteModel(PATIENT_TRACKER_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_EOT_NOTES, PATIENT_TRACKER_ID)
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

    private EotNoteModel getMockedEotNoteModel(final Long patientTrackerId) {
        final EotNoteModel eotNoteModel = new EotNoteModel();
        eotNoteModel.setPatientTrackerId(patientTrackerId);
        eotNoteModel.setNote("Test");
        return eotNoteModel;
    }

    private EotNote getMockedEotNote(final Long patientTrackerId) {
        final EotNoteModel mockedEotNoteModel = getMockedEotNoteModel(patientTrackerId);
        final EotNote eotNote = new EotNote();
        eotNote.setId(mockedEotNoteModel.getPatientTrackerId());
        eotNote.setNote(mockedEotNoteModel.getNote());
        return eotNote;
    }
}
