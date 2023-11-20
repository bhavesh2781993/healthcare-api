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
import com.test.api.healthcare.patients.mappers.PatientVitalsSignMapper;
import com.test.api.healthcare.patients.models.PatientVitalsSignModel;
import com.test.api.healthcare.patients.models.entities.PatientVitalsSign;
import com.test.api.healthcare.patients.services.PatientVitalsSignService;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientVitalsSignControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_VITALS_SIGN = "/patient-vitals-signs";
    private static final String URI_PATIENT_VITALS_SIGN_ID = URI_PATIENT_VITALS_SIGN + "/{patientVitalsSignsId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_VITALS_SIGN_ID =
        "/patient-trackers/{patientTrackerId}" + URI_PATIENT_VITALS_SIGN;

    private static final Long PATIENT_VITALS_SIGN_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;

    @MockBean
    private PatientVitalsSignMapper patientVitalsSignMapper;

    @MockBean
    private PatientVitalsSignService patientVitalsSignService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientVitalsSign() throws Exception {
        final PatientVitalsSignModel patientVitalsSignModel = getMockedPatientVitalsSignModel(PATIENT_VITALS_SIGN_ID);
        when(patientVitalsSignMapper.toPatientVitalsSignModel(any())).thenReturn(patientVitalsSignModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_VITALS_SIGN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientVitalsSignModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("patientVitalsDate").description("Patient Vitals Date"),
                    fieldWithPath("tempInCelcius").description("Temp In Celsius"),
                    fieldWithPath("pulse").description("Pulse"),
                    fieldWithPath("resp").description("Resp"),
                    fieldWithPath("o2StatsInPct").description("o2StatsInPct"),
                    fieldWithPath("bp").description("BP"),
                    fieldWithPath("weightInKg").description("Weight In Kg")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updatePatientVitalsSign() throws Exception {
        final PatientVitalsSignModel patientVitalsSignModel = getMockedPatientVitalsSignModel(PATIENT_VITALS_SIGN_ID);
        when(patientVitalsSignMapper.toPatientVitalsSignModel(any())).thenReturn(patientVitalsSignModel);
        when(patientVitalsSignService.updatePatientVitalsSign(any())).thenReturn(getMockedPatientVitalsSign(PATIENT_VITALS_SIGN_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_VITALS_SIGN_ID, PATIENT_VITALS_SIGN_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientVitalsSignModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientVitalsSignsId").description("Patient Vitals Signs Id")
                ),
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("patientVitalsDate").description("Patient Vitals Date"),
                    fieldWithPath("tempInCelcius").description("Temp In Celsius"),
                    fieldWithPath("pulse").description("Pulse"),
                    fieldWithPath("resp").description("Resp"),
                    fieldWithPath("o2StatsInPct").description("o2StatsInPct"),
                    fieldWithPath("bp").description("BP"),
                    fieldWithPath("weightInKg").description("Weight In Kg")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getPatientVitalsSign() throws Exception {
        when(patientVitalsSignMapper.toPatientVitalsSign(any())).thenReturn(getMockedPatientVitalsSign(PATIENT_VITALS_SIGN_ID));
        when(patientVitalsSignService.getPatientVitalsSign(any()))
            .thenReturn(Optional.of(getMockedPatientVitalsSign(PATIENT_VITALS_SIGN_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_VITALS_SIGN_ID, PATIENT_VITALS_SIGN_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientVitalsSignsId").description("Patient Vitals Signs Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientVitalsSigns() throws Exception {
        final List<PatientVitalsSignModel> patientVitalsSignModels = List.of(getMockedPatientVitalsSignModel(PATIENT_VITALS_SIGN_ID));
        when(patientVitalsSignService.listPatientVitalsSigns(any())).thenReturn(paginatedPatientVitalsSigns(PATIENT_VITALS_SIGN_ID));
        when(patientVitalsSignMapper.toPatientVitalsSignModelList(any())).thenReturn(patientVitalsSignModels);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_VITALS_SIGN_ID, PATIENT_TRACKER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParams(getMockedPaginationRequest()))
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
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletedPatientVitalsSign() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_VITALS_SIGN_ID, PATIENT_VITALS_SIGN_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientVitalsSignsId").description("Patient Vitals Signs Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientVitalsSignModel getMockedPatientVitalsSignModel(final Long patientVitalsSignsId) {
        final PatientVitalsSignModel patientVitalsSignModel = new PatientVitalsSignModel();
        patientVitalsSignModel.setPatientVitalsSignsId(patientVitalsSignsId);
        patientVitalsSignModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientVitalsSignModel.setPatientVitalsDate(LocalDateTime.now());
        patientVitalsSignModel.setTempInCelcius("Temp In Celsius");
        patientVitalsSignModel.setPulse("Pulse");
        patientVitalsSignModel.setResp("Resp");
        patientVitalsSignModel.setO2StatsInPct("O2 Stats In Pct");
        patientVitalsSignModel.setBp("BP");
        patientVitalsSignModel.setWeightInKg("Weight In Kg");
        return patientVitalsSignModel;
    }

    private PatientVitalsSign getMockedPatientVitalsSign(final Long patientVitalsSignsId) {
        final PatientVitalsSignModel patientVitalsSignModel = getMockedPatientVitalsSignModel(patientVitalsSignsId);
        final PatientVitalsSign patientVitalsSign = new PatientVitalsSign();
        patientVitalsSign.setId(patientVitalsSignModel.getPatientVitalsSignsId());
        return patientVitalsSign;
    }

    private List<PatientVitalsSign> listPatientVitalsSign(final Long patientTrackerId) {
        return List.of(getMockedPatientVitalsSign(patientTrackerId));
    }

    private Page<PatientVitalsSign> paginatedPatientVitalsSigns(final Long patientTrackerId) {
        return new PageImpl<>(listPatientVitalsSign(patientTrackerId));
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
