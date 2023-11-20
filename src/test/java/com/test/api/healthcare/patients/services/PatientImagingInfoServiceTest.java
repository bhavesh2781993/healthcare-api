package com.test.api.healthcare.patients.services;

import static com.test.api.healthcare.patients.models.entities.PatientImagingInfo.FK_PATIENT_IMAGING_INFO_ALIGN_TO;
import static com.test.api.healthcare.patients.models.entities.PatientImagingInfo.FK_PATIENT_IMAGING_INFO_IMAGING_TYPE;
import static com.test.api.healthcare.patients.models.entities.PatientImagingInfo.FK_PATIENT_IMAGING_INFO_PATIENT_TRACKER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.configurations.models.entities.ImagingAlignment;
import com.test.api.healthcare.configurations.models.entities.ImagingType;
import com.test.api.healthcare.configurations.services.TreatmentMetaService;
import com.test.api.healthcare.patients.models.entities.PatientImagingInfo;
import com.test.api.healthcare.patients.models.entities.PatientTracker;
import com.test.api.healthcare.patients.repositories.PatientImagingInfoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientImagingInfoServiceTest {

    private static final Long PATIENT_IMAGING_INFO_ID = 1L;
    private static final Long PETIENT_TRACKER_ID = 1L;

    @InjectMocks
    private PatientImagingInfoService patientImagingInfoService;

    @Mock
    private PatientImagingInfoRepository patientImagingInfoRepository;

    @Mock
    private TreatmentMetaService treatmentMetaService;

    private static Stream<Arguments> createAndUpdatePatientImagingInfoExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Patient Tracker ", FK_PATIENT_IMAGING_INFO_PATIENT_TRACKER_ID),
            Arguments.of("Invalid Imaging Type", FK_PATIENT_IMAGING_INFO_IMAGING_TYPE),
            Arguments.of("Invalid Imaging Alignment", FK_PATIENT_IMAGING_INFO_ALIGN_TO)
        );
    }

    @Test
    void listPatientImagingInfoByPatientOrderId() {
        final List<PatientImagingInfo> patientImagingInfo = List.of(getMockedPatientImagingInfo());
        when(patientImagingInfoRepository.findAllByPatientTrackerId(PETIENT_TRACKER_ID)).thenReturn(patientImagingInfo);
        patientImagingInfoService.listPatientImagingInfoByPatientTrackerId(PETIENT_TRACKER_ID);
        verify(patientImagingInfoRepository, times(1)).findAllByPatientTrackerId(PETIENT_TRACKER_ID);
    }

    @Test
    void deletePatientImagingInfo() {
        final PatientImagingInfo patientImagingInfo = getMockedPatientImagingInfo();
        when(patientImagingInfoRepository.findById(any())).thenReturn(Optional.of(patientImagingInfo));
        patientImagingInfoService.deletePatientImagingInfo(patientImagingInfo.getId());
        verify(patientImagingInfoRepository, times(1))
            .findById(PATIENT_IMAGING_INFO_ID);
    }

    @Test
    void deletePatientImagingInfoWhenThrowException() {
        final PatientImagingInfo patientImagingInfo = getMockedPatientImagingInfo();
        when(patientImagingInfoRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> patientImagingInfoService.deletePatientImagingInfo(patientImagingInfo.getId()));
    }

    private PatientImagingInfo getMockedPatientImagingInfo() {
        final PatientImagingInfo patientImagingInfo = new PatientImagingInfo();
        patientImagingInfo.setId(PATIENT_IMAGING_INFO_ID);
        patientImagingInfo.setPatientTracker(getMockedPatientTracker());
        patientImagingInfo.setImagingType(getMockedImagingType());
        patientImagingInfo.setImagingAlignment(getMockedImagingAlignment());
        patientImagingInfo.setNotes("test 1");
        return patientImagingInfo;
    }

    private PatientTracker getMockedPatientTracker() {
        final PatientTracker patientTracker = new PatientTracker();
        patientTracker.setId(1L);
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
}
