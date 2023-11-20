package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientTreatmentInfoMapper;
import com.test.api.healthcare.patients.models.PatientPrescriptionModel;
import com.test.api.healthcare.patients.models.PatientTreatmentInfoModel;
import com.test.api.healthcare.patients.models.entities.PatientTreatmentInfo;
import com.test.api.healthcare.patients.services.PatientTreatmentInfoService;

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
class PatientTreatmentInfoControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_TREATMENT_INFO = "/patient-treatment-info";
    private static final String URI_PATIENT_TREATMENT_INFO_ID = URI_PATIENT_TREATMENT_INFO + "/{patientTreatmentInfoId}";
    private static final String URI_PATIENT_TRACKER_PATIENT_TREATMENT_INFO_ID =
        "/patient-tracker/{patientTrackerId}" + URI_PATIENT_TREATMENT_INFO;

    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_TREATMENT_INFO_ID = 1L;
    private static final Long TREATMENT_SITE_ID = 1L;
    private static final Long TREATMENT_INTENT_ID = 1L;
    private static final Long TREATMENT_MODALITY_ID = 1L;
    private static final Long TREATMENT_LOCATION_ID = 1L;
    private static final Long TREATMENT_MACHINE_ID = 1L;
    private static final Long IMRT_MEDICAL_NECESSITY_ID = 1L;

    //For Patient Prescription
    private static final Long PATIENT_PRESCRIPTION_ID = 1L;
    private static final Long TECHNIQUE_ID = 1L;
    private static final Long FREQUENCY_ID = 1L;
    private static final Long ENERGY_ID = 1L;

    @MockBean
    private PatientTreatmentInfoMapper patientTreatmentInfoMapper;

    @MockBean
    private PatientTreatmentInfoService patientTreatmentInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientTreatmentInfo() throws Exception {
        final PatientTreatmentInfoModel patientTreatmentInfoModel = getMockedPatientTreatmentInfoModel(PATIENT_TREATMENT_INFO_ID);

        when(patientTreatmentInfoMapper.toPatientTreatmentInfoModel(any())).thenReturn(patientTreatmentInfoModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_TREATMENT_INFO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientTreatmentInfoModel(null))))
            .andExpect(status().isCreated())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("patientTrackerId"),
                    fieldWithPath("treatmentSiteId").description("treatmentSiteId"),
                    fieldWithPath("treatmentIntentId").description("treatmentIntentId"),
                    fieldWithPath("treatmentModalityId").description("Treatment Modality Id"),
                    fieldWithPath("imrtMedicalNecessityId").description("Imrt Medical Necessity Id"),
                    fieldWithPath("treatmentLocationId").description("Treatment Location Id"),
                    fieldWithPath("treatmentMachineId").description("Treatment Machine Id"),
                    fieldWithPath("hasConcurrentChemotherapy").description("Has Concurrent Chemotherapy"),
                    fieldWithPath("treatmentStartDate").description("Treatment Start Date"),
                    fieldWithPath("patientPrescriptions").description("Patient Prescriptions")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updatePatientTreatmentInfo() throws Exception {
        final PatientTreatmentInfoModel patientTreatmentInfoModel = getMockedPatientTreatmentInfoModel(PATIENT_TREATMENT_INFO_ID);

        when(patientTreatmentInfoMapper.toPatientTreatmentInfoModel(any())).thenReturn(patientTreatmentInfoModel);
        when(patientTreatmentInfoService.updatePatientTreatmentInfo(any()))
            .thenReturn(getMockedPatientTreatmentInfo(PATIENT_TREATMENT_INFO_ID));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_TREATMENT_INFO_ID, PATIENT_TREATMENT_INFO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientTreatmentInfoModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTreatmentInfoId").description("PatientTreatment Info Id")
                ),
                relaxedRequestFields(
                    fieldWithPath("patientTrackerId").description("patientTrackerId"),
                    fieldWithPath("treatmentSiteId").description("treatmentSiteId"),
                    fieldWithPath("treatmentIntentId").description("treatmentIntentId"),
                    fieldWithPath("treatmentModalityId").description("Treatment Modality Id"),
                    fieldWithPath("imrtMedicalNecessityId").description("Imrt Medical Necessity Id"),
                    fieldWithPath("treatmentLocationId").description("Treatment Location Id"),
                    fieldWithPath("treatmentMachineId").description("Treatment Machine Id"),
                    fieldWithPath("hasConcurrentChemotherapy").description("Has Concurrent Chemotherapy"),
                    fieldWithPath("treatmentStartDate").description("Treatment Start Date"),
                    fieldWithPath("patientPrescriptions").description("Patient Prescriptions")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void getPatientTreatmentInfo() throws Exception {
        final PatientTreatmentInfoModel patientTreatmentInfoModel = getMockedPatientTreatmentInfoModel(PATIENT_TREATMENT_INFO_ID);
        when(patientTreatmentInfoMapper.toPatientTreatmentInfoModel(any())).thenReturn(patientTreatmentInfoModel);
        when(patientTreatmentInfoService.getPatientTreatmentInfo(any()))
            .thenReturn(Optional.of(getMockedPatientTreatmentInfo(PATIENT_TREATMENT_INFO_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TREATMENT_INFO_ID, PATIENT_TREATMENT_INFO_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTreatmentInfoId").description("PatientTreatment Info Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientTreatmentInfo() throws Exception {
        final List<PatientTreatmentInfoModel> patientTreatmentInfoModels =
            List.of(getMockedPatientTreatmentInfoModel(PATIENT_TREATMENT_INFO_ID));

        when(patientTreatmentInfoMapper.toPatientTreatmentInfoList(any())).thenReturn(patientTreatmentInfoModels);
        when(patientTreatmentInfoService.listPatientTreatmentInfo(any()))
            .thenReturn(List.of(getMockedPatientTreatmentInfo(PATIENT_TREATMENT_INFO_ID)));

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_PATIENT_TREATMENT_INFO_ID, PATIENT_TRACKER_ID)
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
    void deletePatientTreatmentInfo() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_TREATMENT_INFO_ID, PATIENT_TREATMENT_INFO_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTreatmentInfoId").description("PatientTreatment Info Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientTreatmentInfoModel getMockedPatientTreatmentInfoModel(final Long patientTreatmentInfoId) {
        final PatientTreatmentInfoModel patientTreatmentInfoModel = new PatientTreatmentInfoModel();
        patientTreatmentInfoModel.setPatientTreatmentInfoId(patientTreatmentInfoId);
        patientTreatmentInfoModel.setPatientTrackerId(PATIENT_TRACKER_ID);
        patientTreatmentInfoModel.setTreatmentSiteId(TREATMENT_SITE_ID);
        patientTreatmentInfoModel.setTreatmentIntentId(TREATMENT_INTENT_ID);
        patientTreatmentInfoModel.setTreatmentModalityId(TREATMENT_MODALITY_ID);
        patientTreatmentInfoModel.setImrtMedicalNecessityId(IMRT_MEDICAL_NECESSITY_ID);
        patientTreatmentInfoModel.setTreatmentLocationId(TREATMENT_LOCATION_ID);
        patientTreatmentInfoModel.setTreatmentMachineId(TREATMENT_MACHINE_ID);
        patientTreatmentInfoModel.setHasConcurrentChemotherapy(true);
        patientTreatmentInfoModel.setTreatmentStartDate(LocalDateTime.now());
        patientTreatmentInfoModel.setPatientPrescriptions(List.of(getMockedPatientPrescriptionModel()));
        return patientTreatmentInfoModel;
    }

    private PatientTreatmentInfo getMockedPatientTreatmentInfo(final Long patientTreatmentInfoId) {
        final PatientTreatmentInfoModel patientTreatmentInfoModel = getMockedPatientTreatmentInfoModel(patientTreatmentInfoId);
        final PatientTreatmentInfo patientTreatmentInfo = new PatientTreatmentInfo();
        patientTreatmentInfo.setId(patientTreatmentInfoModel.getPatientTreatmentInfoId());
        return patientTreatmentInfo;
    }

    private PatientPrescriptionModel getMockedPatientPrescriptionModel() {
        final PatientPrescriptionModel patientPrescriptionModel = new PatientPrescriptionModel();

        patientPrescriptionModel.setPatientPrescriptionId(PATIENT_PRESCRIPTION_ID);
        patientPrescriptionModel.setPatientTreatmentInfoId(PATIENT_TREATMENT_INFO_ID);
        patientPrescriptionModel.setTechniqueId(TECHNIQUE_ID);
        patientPrescriptionModel.setFrequencyId(FREQUENCY_ID);
        patientPrescriptionModel.setEnergyId(ENERGY_ID);
        patientPrescriptionModel.setCourseName("Course 1");
        patientPrescriptionModel.setTargetVolume("targetVolume 1");
        patientPrescriptionModel.setDosePerFractionInCgy(1);
        patientPrescriptionModel.setTotalDoseInCgy(1);
        patientPrescriptionModel.setTotalDoseDeliveredOInCgy(1);
        patientPrescriptionModel.setTotalFractions(1);
        patientPrescriptionModel.setTotalFractionsDelivered(1);
        patientPrescriptionModel.setSequence(1);
        return patientPrescriptionModel;
    }
}
