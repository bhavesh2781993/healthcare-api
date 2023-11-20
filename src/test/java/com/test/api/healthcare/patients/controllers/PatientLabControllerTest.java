package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientLabMapper;
import com.test.api.healthcare.patients.models.PatientLabModel;
import com.test.api.healthcare.patients.services.PatientLabService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientLabControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_LAB = "/patient-labs";
    private static final String URI_PATIENT_LAB_ID = URI_PATIENT_LAB + "/{patientLabId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_LAB_ID = "/patient-tracker/{patientTrackerId}" + URI_PATIENT_LAB;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_LAB_ID = 1L;

    @MockBean
    private PatientLabMapper patientLabMapper;

    @MockBean
    private PatientLabService patientLabService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientLab() throws Exception {
        final PatientLabModel patientLabModel = getMockedPatientLabModel(PATIENT_LAB_ID);

        when(patientLabMapper.toPatientLabModel(any())).thenReturn(patientLabModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_LAB)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientLabModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("labName").description("Lab Name"),
                    fieldWithPath("labDate").description("Lab Date"),
                    fieldWithPath("labUrl").description("Lab Url")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientLabs() throws Exception {

        final List<PatientLabModel> patientLabModels = List.of(getMockedPatientLabModel(PATIENT_LAB_ID));
        when(patientLabMapper.toPatientLabModelList(any())).thenReturn(patientLabModels);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_LAB_ID, PATIENT_TRACKER_ID)
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
    void deletePatientLab() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_LAB_ID, PATIENT_LAB_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientLabId").description("Patient Lab Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updatePatientLab() throws Exception {
        final PatientLabModel patientLabModel = getMockedPatientLabModel(PATIENT_LAB_ID);

        when(patientLabMapper.toPatientLabModel(any())).thenReturn(patientLabModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_LAB_ID, PATIENT_LAB_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientLabModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("labName").description("Lab Name"),
                    fieldWithPath("labDate").description("Lab Date"),
                    fieldWithPath("labUrl").description("Lab Url")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientLabModel getMockedPatientLabModel(final Long patientLabId) {
        final PatientLabModel patientLabModel = new PatientLabModel();
        patientLabModel.setPatientLabId(patientLabId);
        patientLabModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientLabModel.setLabDate(LocalDateTime.now());
        patientLabModel.setLabName("EkZero");
        patientLabModel.setLabUrl("Url 1");
        return patientLabModel;
    }
}
