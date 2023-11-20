package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.patients.mappers.PatientPrescriptionMapper;
import com.test.api.healthcare.patients.models.PatientPrescriptionModel;
import com.test.api.healthcare.patients.models.entities.PatientPrescription;
import com.test.api.healthcare.patients.services.PatientPrescriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientPrescriptionControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_PRESCRIPTION = "/patient-prescriptions";
    private static final String URI_PATIENT_PRESCRIPTION_ID = URI_PATIENT_PRESCRIPTION + "/{patientPrescriptionId}";
    private static final String URI_PATIENT_TREATMENT_INFO_PATIENT_PRESCRIPTION_ID =
        "/patient-treatment-info/{patientTreatmentInfoId}" + URI_PATIENT_PRESCRIPTION;

    private static final Long PATIENT_PRESCRIPTION_ID = 1L;
    private static final Long PATIENT_TREATMENT_INFO_ID = 1L;
    private static final Long FREQUENCY_ID = 1L;
    private static final Long ENERGY_ID = 1L;
    private static final Long TECHNIQUE_ID = 1L;


    @MockBean
    private PatientPrescriptionMapper patientPrescriptionMapper;

    @MockBean
    private PatientPrescriptionService patientPrescriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatientPrescription() throws Exception {
        final PatientPrescriptionModel patientPrescriptionModel = getMockedPatientPrescriptionModel(PATIENT_PRESCRIPTION_ID);
        when(patientPrescriptionMapper.toPatientPrescriptionModel(any())).thenReturn(patientPrescriptionModel);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(URI_PATIENT_PRESCRIPTION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientPrescriptionModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                relaxedRequestFields(
                    fieldWithPath("patientTreatmentInfoId").description("Patient Treatment Info Id"),
                    fieldWithPath("techniqueId").description("Technique Id"),
                    fieldWithPath("frequencyId").description("Frequency Id"),
                    fieldWithPath("energyId").description("Energy Id"),
                    fieldWithPath("courseName").description("Course Name"),
                    fieldWithPath("targetVolume").description("Target Volume"),
                    fieldWithPath("dosePerFractionInCgy").description("Dose Per Fraction In Cgy"),
                    fieldWithPath("totalDoseInCgy").description("Total Dose In Cgy"),
                    fieldWithPath("totalDoseDeliveredOInCgy").description("Total Dose Delivered In Cgy"),
                    fieldWithPath("totalFractions").description("Total Fractions"),
                    fieldWithPath("totalFractionsDelivered").description("Total Fractions Delivered"),
                    fieldWithPath("sequence").description("Sequence")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void listPatientPrescriptions() throws Exception {
        final List<PatientPrescriptionModel> patientPrescriptionModels = listPatientPrescriptionModels();
        final List<PatientPrescription> patientPrescriptions = listPatientPrescription();

        when(patientPrescriptionMapper.toPatientPrescriptionModelList(any())).thenReturn(patientPrescriptionModels);
        when(patientPrescriptionService.listPatientPrescriptionsByTrackerId(any())).thenReturn(patientPrescriptions);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(URI_PATIENT_TREATMENT_INFO_PATIENT_PRESCRIPTION_ID, PATIENT_TREATMENT_INFO_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientTreatmentInfoId").description("Patient Treatment Info Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientPrescription() throws Exception {

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(URI_PATIENT_PRESCRIPTION_ID, PATIENT_PRESCRIPTION_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientPrescriptionId").description("Patient Prescription Id")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void updatePatientPrescription() throws Exception {
        final PatientPrescriptionModel patientPrescriptionModel = getMockedPatientPrescriptionModel(PATIENT_PRESCRIPTION_ID);
        final PatientPrescription patientPrescription = getMockedPatientPrescription();

        when(patientPrescriptionMapper.toPatientPrescriptionModel(any())).thenReturn(patientPrescriptionModel);
        when(patientPrescriptionService.updatePatientPrescription(any())).thenReturn(patientPrescription);

        final MvcResult mvcResult = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(URI_PATIENT_PRESCRIPTION_ID, PATIENT_PRESCRIPTION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getMockedPatientPrescriptionModel(null))))
            .andExpect(status().isOk())
            .andDo(documentationHandler.document(
                pathParameters(
                    parameterWithName("patientPrescriptionId").description("Patient Prescription Id")
                ),
                relaxedRequestFields(
                    fieldWithPath("patientTreatmentInfoId").description("Patient Treatment Info Id"),
                    fieldWithPath("techniqueId").description("Technique Id"),
                    fieldWithPath("frequencyId").description("Frequency Id"),
                    fieldWithPath("energyId").description("Energy Id"),
                    fieldWithPath("courseName").description("Course Name"),
                    fieldWithPath("targetVolume").description("Target Volume"),
                    fieldWithPath("dosePerFractionInCgy").description("Dose Per Fraction In Cgy"),
                    fieldWithPath("totalDoseInCgy").description("Total Dose In Cgy"),
                    fieldWithPath("totalDoseDeliveredOInCgy").description("Total Dose Delivered In Cgy"),
                    fieldWithPath("totalFractions").description("Total Fractions"),
                    fieldWithPath("totalFractionsDelivered").description("Total Fractions Delivered"),
                    fieldWithPath("sequence").description("Sequence")
                )
            ))
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientPrescriptionModel getMockedPatientPrescriptionModel(final Long patientPrescriptionId) {
        final PatientPrescriptionModel patientPrescriptionModel = new PatientPrescriptionModel();
        patientPrescriptionModel.setPatientPrescriptionId(patientPrescriptionId);
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

    private PatientPrescription getMockedPatientPrescription() {
        final PatientPrescriptionModel patientPrescriptionModel = getMockedPatientPrescriptionModel(
            PatientPrescriptionControllerTest.PATIENT_PRESCRIPTION_ID);
        final PatientPrescription patientPrescription = new PatientPrescription();
        patientPrescription.setId(patientPrescriptionModel.getPatientPrescriptionId());
        return patientPrescription;
    }

    private List<PatientPrescriptionModel> listPatientPrescriptionModels() {
        return List.of(getMockedPatientPrescriptionModel(PatientPrescriptionControllerTest.PATIENT_PRESCRIPTION_ID));
    }

    private List<PatientPrescription> listPatientPrescription() {
        return List.of(getMockedPatientPrescription());
    }
}
