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
import com.test.api.healthcare.patients.constants.AppointmentStatus;
import com.test.api.healthcare.patients.constants.AppointmentType;
import com.test.api.healthcare.patients.mappers.PatientAppointmentMapper;
import com.test.api.healthcare.patients.models.PatientAppointmentModel;
import com.test.api.healthcare.patients.models.entities.PatientAppointment;
import com.test.api.healthcare.patients.services.PatientAppointmentService;

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
class PatientAppointmentControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_APPOINTMENT = "/patient-appointments";
    private static final String URI_PATIENT_APPOINTMENT_ID = URI_PATIENT_APPOINTMENT + "/{patientAppointmentId}";

    private static final String HEADER_CLINIC_ID_KEY = "x-no-clinic-id";
    private static final Long HEADER_CLINIC_ID_VALUE = 1L;

    private static final Long PATIENT_APPOINTMENT_ID = 1L;
    private static final Long PATIENT_ID = 1L;
    private static final Long CLINIC_LOCATION_ID = 1L;
    private static final Long SCHEDULED_BY_ID = 1L;
    private static final Long SCHEDULED_WITH_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_TRACKER_STEP_ID = 1L;


    @MockBean
    private PatientAppointmentMapper patientAppointmentMapper;

    @MockBean
    private PatientAppointmentService patientAppointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientAppointment() throws Exception {
        final PatientAppointmentModel patientAppointmentModel = getMockedPatientAppointmentModel(PATIENT_APPOINTMENT_ID);
        when(patientAppointmentMapper.toPatientAppointmentModel(any())).thenReturn(patientAppointmentModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_APPOINTMENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientAppointmentModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientId").description("Patient Id"),
                    fieldWithPath("clinicLocationId").description("Clinic Location Id"),
                    fieldWithPath("scheduledDatetime").description("Scheduled Date Time"),
                    fieldWithPath("scheduledBy").description("Scheduled By"),
                    fieldWithPath("scheduledWith").description("Scheduled With"),
                    fieldWithPath("appointmentType").description("Appointment Type"),
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("patientTrackerStepId").description("Patient Tracker Step Id"),
                    fieldWithPath("appointmentStatus").description("Appointment Status")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updatePatientAppointment() throws Exception {
        final PatientAppointmentModel patientAppointmentModel = getMockedPatientAppointmentModel(PATIENT_APPOINTMENT_ID);
        when(patientAppointmentMapper.toPatientAppointmentModel(any())).thenReturn(patientAppointmentModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_APPOINTMENT_ID, PATIENT_APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientAppointmentModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientId").description("Patient Id"),
                    fieldWithPath("clinicLocationId").description("Clinic Location Id"),
                    fieldWithPath("scheduledDatetime").description("Scheduled Date Time"),
                    fieldWithPath("scheduledBy").description("Scheduled By"),
                    fieldWithPath("scheduledWith").description("Scheduled With"),
                    fieldWithPath("appointmentType").description("Appointment Type"),
                    fieldWithPath("patientTrackerId").description("Patient Tracker Id"),
                    fieldWithPath("patientTrackerStepId").description("Patient Tracker Step Id"),
                    fieldWithPath("appointmentStatus").description("Appointment Status")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getPatientAppointment() throws Exception {
        final PatientAppointmentModel patientAppointmentModel = getMockedPatientAppointmentModel(PATIENT_APPOINTMENT_ID);
        when(patientAppointmentService.getPatientAppointment(any()))
            .thenReturn(Optional.of(getMockedPatientAppointment(PATIENT_APPOINTMENT_ID)));
        when(patientAppointmentMapper.toPatientAppointmentModel(any())).thenReturn(patientAppointmentModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_APPOINTMENT_ID, PATIENT_APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientAppointmentId").description("Patient Appointment Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientAppointments() throws Exception {
        final List<PatientAppointmentModel> patientAppointmentModels = List.of(getMockedPatientAppointmentModel(PATIENT_APPOINTMENT_ID));
        when(patientAppointmentMapper.toPatientAppointmentModelList(any())).thenReturn(patientAppointmentModels);
        when(patientAppointmentService.listPatientAppointments(any()))
            .thenReturn(paginatedMockedPatientAppointments(PATIENT_APPOINTMENT_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_APPOINTMENT)
                .header(HEADER_CLINIC_ID_KEY, HEADER_CLINIC_ID_VALUE)
                .queryParams(getMockedPaginationRequest())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                queryParameters(
                    parameterWithName("filter").description("Field on which filter is requested"),
                    parameterWithName("sort").description("Direction of sort field. Allowed values: [ASC, DESC]"),
                    parameterWithName("pageNo").description("Page no for which resources are requested. pageNo starts at: 0"),
                    parameterWithName("pageSize").description("No of elements in a page. Max pageSize limit: 100")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientAppointment() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_APPOINTMENT_ID, PATIENT_APPOINTMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientAppointmentId").description("Patient Appointment Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientAppointmentModel getMockedPatientAppointmentModel(final Long patientAppointmentId) {
        final PatientAppointmentModel patientAppointmentModel = new PatientAppointmentModel();
        patientAppointmentModel.setPatientAppointmentId(patientAppointmentId);
        patientAppointmentModel.setParentPatientAppointmentId(patientAppointmentId);
        patientAppointmentModel.setPatientId(PATIENT_ID);
        patientAppointmentModel.setClinicLocationId(CLINIC_LOCATION_ID);
        patientAppointmentModel.setScheduledDatetime(LocalDateTime.now());
        patientAppointmentModel.setScheduledBy(SCHEDULED_BY_ID);
        patientAppointmentModel.setScheduledWith(SCHEDULED_WITH_ID);
        patientAppointmentModel.setAppointmentType(AppointmentType.RADIATION.name());
        patientAppointmentModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientAppointmentModel.setPatientTrackerStepId(PATIENT_TRACKER_STEP_ID);
        patientAppointmentModel.setAppointmentStatus(AppointmentStatus.COMPLETED.name());
        return patientAppointmentModel;
    }

    private PatientAppointment getMockedPatientAppointment(final Long patientAppointmentId) {
        final PatientAppointmentModel mockedPatientAppointment = getMockedPatientAppointmentModel(patientAppointmentId);
        final PatientAppointment patientAppointment = new PatientAppointment();
        patientAppointment.setId(mockedPatientAppointment.getPatientAppointmentId());
        return patientAppointment;
    }

    private List<PatientAppointment> listMockedPatientAppointments(final Long patientAppointmentId) {
        return List.of(getMockedPatientAppointment(patientAppointmentId));
    }

    private Page<PatientAppointment> paginatedMockedPatientAppointments(final Long patientAppointmentId) {
        return new PageImpl<>(listMockedPatientAppointments(patientAppointmentId));
    }

    private MultiValueMap<String, String> getMockedPaginationRequest() {
        final LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("filter", "name");
        linkedMultiValueMap.add("sort", "asc");
        linkedMultiValueMap.add("pageNo", "0");
        linkedMultiValueMap.add("pageSize", "1");
        return linkedMultiValueMap;
    }
}