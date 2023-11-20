package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.ImmobilizationDeviceQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.ImmobilizationDevice;
import com.test.api.healthcare.configurations.repositories.ImmobilizationDeviceRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImmobilizationDeviceServiceTest {

    private static final Long IMMOBILIZATION_DEVICE_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String IMMOBILIZATION_DEVICE = "Immobilization device";

    @InjectMocks
    ImmobilizationDeviceService immobilizationDeviceService;

    @Mock
    ImmobilizationDeviceRepository immobilizationDeviceRepository;

    private static Stream<Arguments> createImmobilizationDeviceExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_immobilization_device_lookup_clinic_id")
        );
    }

    @Test
    void createImmobilizationDevice() {
        final ImmobilizationDevice immobilizationDevice = getMockImmobilizationDevice();
        when(immobilizationDeviceRepository.save(any())).thenReturn(immobilizationDevice);
        final ImmobilizationDevice immobilizationDeviceResponse =
            immobilizationDeviceService.createImmobilizationDevice(immobilizationDevice);
        Assertions.assertNotNull(immobilizationDeviceResponse);
    }

    @ParameterizedTest
    @MethodSource("createImmobilizationDeviceExceptionSource")
    void createImmobilizationDeviceWhenThrowException(final String message, final String constraintName) {
        final ImmobilizationDevice immobilizationDevice = getMockImmobilizationDevice();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(immobilizationDeviceRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> immobilizationDeviceService.createImmobilizationDevice(immobilizationDevice));
    }

    @Test
    void listImmobilizationDevices() {
        final List<ImmobilizationDevice> immobilizationDevice = getMockImmobilizationDeviceList();
        final ImmobilizationDeviceQueryParam immobilizationDeviceQueryParam = ImmobilizationDeviceQueryParam.builder()
            .clinicId(CLINIC_ID)
            .build();
        when(immobilizationDeviceRepository.findAllByClinicId(immobilizationDeviceQueryParam.getClinicId()))
            .thenReturn(immobilizationDevice);
        final List<ImmobilizationDevice> immobilizationDeviceListResponse =
            immobilizationDeviceService.listImmobilizationDevices(immobilizationDeviceQueryParam);
        Assertions.assertEquals(1, immobilizationDeviceListResponse.size());
    }

    @Test
    void deleteImmobilizationDevice() {
        final ImmobilizationDevice immobilizationDevice = getMockImmobilizationDevice();
        when(immobilizationDeviceRepository.findByIdAndClinicId(immobilizationDevice.getId(), CLINIC_ID))
            .thenReturn(Optional.of(immobilizationDevice));
        immobilizationDeviceService.deleteImmobilizationDevice(immobilizationDevice.getId(), CLINIC_ID);
        verify(immobilizationDeviceRepository, times(1))
            .findByIdAndClinicId(immobilizationDevice.getId(), CLINIC_ID);
    }

    @Test
    void deleteImmobilizationDeviceWhenReturnNull() {
        final ImmobilizationDevice immobilizationDevice = getMockImmobilizationDevice();
        when(immobilizationDeviceRepository.findByIdAndClinicId(immobilizationDevice.getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataValidationException.class,
            () -> immobilizationDeviceService.deleteImmobilizationDevice(IMMOBILIZATION_DEVICE_ID, CLINIC_ID));
    }

    private ImmobilizationDevice getMockImmobilizationDevice() {
        final ImmobilizationDevice immobilizationDevice = new ImmobilizationDevice();
        immobilizationDevice.setId(IMMOBILIZATION_DEVICE_ID);
        immobilizationDevice.setClinic(getMockedClinic());
        immobilizationDevice.setDevice(IMMOBILIZATION_DEVICE);
        return immobilizationDevice;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<ImmobilizationDevice> getMockImmobilizationDeviceList() {
        return List.of(getMockImmobilizationDevice());
    }
}
