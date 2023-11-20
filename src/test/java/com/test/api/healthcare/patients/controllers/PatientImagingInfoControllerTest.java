package com.test.api.healthcare.patients.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.api.healthcare.BaseDocumentationTest;
import com.test.api.healthcare.configurations.models.entities.ImagingAlignment;
import com.test.api.healthcare.configurations.models.entities.ImagingType;
import com.test.api.healthcare.patients.mappers.PatientImagingInfoMapper;
import com.test.api.healthcare.patients.models.PatientImagingInfoModel;
import com.test.api.healthcare.patients.models.entities.PatientImagingInfo;
import com.test.api.healthcare.patients.models.entities.PatientTracker;
import com.test.api.healthcare.patients.services.PatientImagingInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
class PatientImagingInfoControllerTest extends BaseDocumentationTest {

    private static final String URI_PATIENT_IMAGING_INFO = "/patient-imaging-info";
    private static final String URI_PATIENT_TRACKER_IMAGING_INFO = "/patient-trackers/{patientTrackerId}/patient-imaging-info";
    private static final String URI_PATIENT_IMAGING_INFO_ID = URI_PATIENT_IMAGING_INFO + "/{patientImagingInfoId}";

    private static final Long PATIENT_IMAGING_INFO_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final Long PATIENT_IMAGING_TYPE_ID = 1L;
    private static final Long PATIENT_IMAGING_ALIGNMENT_ID = 1L;

    @MockBean
    private PatientImagingInfoMapper patientImagingInfoMapper;

    @MockBean
    private PatientImagingInfoService patientImagingInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listPatientImagingInfo() throws Exception {
        when(patientImagingInfoMapper.toPatientImagingInfoModelList(anyList())).thenReturn(getMockedPatientImagingInfoModelList());
        final MvcResult mvcResult = mockMvc
            .perform(RestDocumentationRequestBuilders
                .get(URI_PATIENT_TRACKER_IMAGING_INFO, PATIENT_TRACKER_ID)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    @Test
    void deletePatientImagingInfo() throws Exception {
        when(patientImagingInfoMapper.toPatientImagingInfoModel(any()))
            .thenReturn(getMockedPatientImagingInfoModel(PATIENT_IMAGING_INFO_ID));
        final MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders
            .delete(URI_PATIENT_IMAGING_INFO_ID, PATIENT_IMAGING_INFO_ID)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andReturn();
        Assertions.assertNotNull(mvcResult);
    }

    private PatientImagingInfoModel getMockedPatientImagingInfoModel(final Long patientImagingInfoId) {
        final PatientImagingInfoModel patientImagingInfoModel = new PatientImagingInfoModel();
        patientImagingInfoModel.setPatientImagingInfoId(patientImagingInfoId);
        patientImagingInfoModel.setImagingTypeId(PATIENT_IMAGING_TYPE_ID);
        patientImagingInfoModel.setImagingAlignmentId(PATIENT_IMAGING_ALIGNMENT_ID);
        patientImagingInfoModel.setNotes("TEST 1");
        return patientImagingInfoModel;
    }

    private PatientImagingInfo getMockedPatientImagingInfo(final Long patientImagingInfoId) {
        final PatientImagingInfo patientImagingInfo = new PatientImagingInfo();
        patientImagingInfo.setId(patientImagingInfoId);
        patientImagingInfo.setPatientTracker(getMockedPatientTracker());
        patientImagingInfo.setImagingType(getMockedImagingType());
        patientImagingInfo.setImagingAlignment(getMockedImagingAlignment());
        patientImagingInfo.setNotes("Test");
        return patientImagingInfo;
    }

    private PatientTracker getMockedPatientTracker() {
        final PatientTracker patientTracker = new PatientTracker();
        patientTracker.setId(PATIENT_TRACKER_ID);
        return patientTracker;
    }

    private ImagingType getMockedImagingType() {
        final ImagingType imagingType = new ImagingType();
        imagingType.setImagingType("Imaging Type");
        return imagingType;
    }

    private ImagingAlignment getMockedImagingAlignment() {
        final ImagingAlignment imagingAlignment = new ImagingAlignment();
        imagingAlignment.setAlignTo("Align to");
        return imagingAlignment;
    }

    private List<PatientImagingInfoModel> getMockedPatientImagingInfoModelList() {
        return List.of(getMockedPatientImagingInfoModel(PATIENT_IMAGING_INFO_ID));
    }
}
