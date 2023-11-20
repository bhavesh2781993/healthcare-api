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
import com.test.api.healthcare.patients.mappers.PatientMapper;
import com.test.api.healthcare.patients.models.PatientModel;
import com.test.api.healthcare.patients.models.PatientNoteModel;
import com.test.api.healthcare.patients.models.PatientOncologySummaryModel;
import com.test.api.healthcare.patients.models.PatientTreatmentStageModel;
import com.test.api.healthcare.patients.models.StatusModel;
import com.test.api.healthcare.patients.models.TreatmentTeamModel;
import com.test.api.healthcare.patients.models.entities.Patient;
import com.test.api.healthcare.patients.models.responses.PatientDataResponse;
import com.test.api.healthcare.patients.services.PatientDetailAggregator;
import com.test.api.healthcare.patients.services.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENTS = "/patients";
    private static final String URI_PATIENT_ID = URI_PATIENTS + "/{id}";

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final Long HEADER_CLINIC_ID_VALUE = 1L;

    private static final Long PATIENT_ID = 1L;
    private static final Long PHYSICIAN_ID = 1L;

    @MockBean
    private PatientMapper patientMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientDetailAggregator patientDetailAggregator;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createPatient() throws Exception {
        when(patientMapper.toPatientModel(any())).thenReturn(getMockedPatientModel(PATIENT_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENTS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("mrn").description("mrn number"),
                    fieldWithPath("firstName").description("First Name of Patient"),
                    fieldWithPath("middleName").description("Middle Name of Patient"),
                    fieldWithPath("lastName").description("Last Name of Patient"),
                    fieldWithPath("dob").description("Date of Birth of Patient"),
                    fieldWithPath("ssn").description("ssn number"),
                    fieldWithPath("email").description("Email address of Patient"),
                    fieldWithPath("address").description("Address of Patient"),
                    fieldWithPath("physicianId").description("Physician Id"),
                    fieldWithPath("firstConsultDate").description("First Consult Date")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void updatePatient() throws Exception {
        when(patientService.getPatient(anyLong())).thenReturn(Optional.of(getMockedPatient(PATIENT_ID)));
        when(patientMapper.toPatient(any(), any())).thenReturn(getMockedPatient(PATIENT_ID));
        when(patientMapper.toPatientModel(any())).thenReturn(getMockedPatientModel(PATIENT_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_ID, PATIENT_ID)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("mrn").description("mrn number"),
                    fieldWithPath("firstName").description("First Name of Patient"),
                    fieldWithPath("middleName").description("Middle Name of Patient"),
                    fieldWithPath("lastName").description("Last Name of Patient"),
                    fieldWithPath("dob").description("Date of Birth of Patient"),
                    fieldWithPath("ssn").description("ssn number"),
                    fieldWithPath("email").description("email address of Patient"),
                    fieldWithPath("address").description("address of Patient"),
                    fieldWithPath("physicianId").description("Physician Id"),
                    fieldWithPath("firstConsultDate").description("First Consult Date")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void getPatientDetails() throws Exception {
        when(patientDetailAggregator.getPatientDetails(anyLong())).thenReturn(getMockedPatientDataResponse(PATIENT_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_ID, PATIENT_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("id").description("Patient id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    @Test
    void listPatients() throws Exception {
        when(patientService.listPatients(any())).thenReturn(paginatedMockedPatients(PATIENT_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENTS)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParams(getMockedPaginationRequest())
                .contentType(APPLICATION_JSON))
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

        assertNotNull(mvcResult);
    }

    @Test
    void deletePatient() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_ID, PATIENT_ID))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("id").description("Patient id")
                )
            ))
            .andReturn();

        assertNotNull(mvcResult);
    }

    private PatientModel getMockedPatientModel(final Long patientId) {
        final PatientModel patientModel = new PatientModel();
        patientModel.setPatientId(patientId);
        patientModel.setMrn("Test MRN");
        patientModel.setFirstName("Test First Name");
        patientModel.setMiddleName("Test Middle Name");
        patientModel.setLastName("Test Last Name");
        patientModel.setSsn("Test ssn");
        patientModel.setEmail("Test Email");
        patientModel.setAddress("Test Address");
        patientModel.setDob(LocalDate.now());
        patientModel.setPhysicianId(PHYSICIAN_ID);
        patientModel.setFirstConsultDate(LocalDateTime.now());
        return patientModel;
    }

    private Patient getMockedPatient(final Long patientId) {
        final PatientModel mockedPatientModel = getMockedPatientModel(patientId);
        final Patient patient = new Patient();
        patient.setId(mockedPatientModel.getPatientId());
        patient.setMrn(mockedPatientModel.getMrn());
        return patient;
    }

    private PatientDataResponse getMockedPatientDataResponse(final Long patientId) {
        final PatientModel mockedPatientModel = getMockedPatientModel(patientId);
        final PatientDataResponse response = new PatientDataResponse();
        response.setPatient(mockedPatientModel);
        response.setTreatmentTeams(Collections.singletonList(new TreatmentTeamModel()));
        response.setPatientTreatmentStages(Collections.singletonList(new PatientTreatmentStageModel()));
        response.setOncologySummary(new PatientOncologySummaryModel());
        response.setPatientNotes(Collections.singletonList(new PatientNoteModel()));
        response.setPreSimulationVisit(new StatusModel());
        response.setPreTreatmentWorkup(new StatusModel());
        response.setPhysicianOrders(new StatusModel());
        response.setNursingTasks(new StatusModel());
        response.setSimulationStatus(new StatusModel());
        response.setReSimulationStatus(new StatusModel());
        return response;
    }

    private List<Patient> listMockedPatients(final Long patientId) {
        return List.of(getMockedPatient(patientId));
    }

    private Page<Patient> paginatedMockedPatients(final Long patientId) {
        return new PageImpl<>(listMockedPatients(patientId));
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
