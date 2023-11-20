package com.test.api.healthcare.patients.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_MEDICAL_PHYSICS_CONSULT_NOT_FOUND_FOR_ID;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_SIM_ORDER_NOT_FOUND;
import static com.test.api.healthcare.patients.models.entities.SimMedicalPhysicsConsult.FK_SIM_SPECIAL_MEDICAL_PHYSICS_CONSULT_ID;
import static com.test.api.healthcare.patients.models.entities.SimMedicalPhysicsConsult.FK_SIM_SPECIAL_MEDICAL_PHYSICS_CONSULT_SIM_ORDER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.configurations.models.entities.MedicalPhysicsConsult;
import com.test.api.healthcare.patients.models.entities.SimMedicalPhysicsConsult;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.repositories.SimMedicalPhysicsConsultRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

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
class SimMedicalPhysicsConsultServiceTest {

    private static final Long SIM_MEDICAL_PHYSICS_CONSULTS_ID = 1L;

    @InjectMocks
    private SimMedicalPhysicsConsultService simMedicalPhysicsConsultService;

    @Mock
    private SimMedicalPhysicsConsultRepository simMedicalPhysicsConsultRepository;

    @Test
    void createOrUpdateSimMedicalPhysicsConsult() {
        final SimMedicalPhysicsConsult simMedicalPhysicsConsult = getMockedSimMedicalPhysicsConsult();
        when(simMedicalPhysicsConsultRepository.save(any())).thenReturn(simMedicalPhysicsConsult);
        final SimMedicalPhysicsConsult simMedicalPhysicsConsultResponse =
            simMedicalPhysicsConsultService.createOrUpdateSimMedicalPhysicsConsult(simMedicalPhysicsConsult);
        Assertions.assertNotNull(simMedicalPhysicsConsultResponse);
    }

    @ParameterizedTest
    @MethodSource("createOrUpdateSimMedicalPhysicsConsultExceptionSource")
    void createOrUpdateSimMedicalPhysicsConsultWhenThrowException(final String message, final String constraintName) {
        final SimMedicalPhysicsConsult simMedicalPhysicsConsult = getMockedSimMedicalPhysicsConsult();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(simMedicalPhysicsConsultRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(DataNotFoundException.class,
            () -> simMedicalPhysicsConsultService.createOrUpdateSimMedicalPhysicsConsult(simMedicalPhysicsConsult));
    }

    private static Stream<Arguments> createOrUpdateSimMedicalPhysicsConsultExceptionSource() {
        return Stream.of(
            Arguments.of(ERR_MSG_SIM_ORDER_NOT_FOUND, FK_SIM_SPECIAL_MEDICAL_PHYSICS_CONSULT_SIM_ORDER_ID),
            Arguments.of(ERR_MSG_MEDICAL_PHYSICS_CONSULT_NOT_FOUND_FOR_ID, FK_SIM_SPECIAL_MEDICAL_PHYSICS_CONSULT_ID)
        );
    }

    @Test
    void getSimMedicalPhysicsConsult() {
        final SimMedicalPhysicsConsult simMedicalPhysicsConsult = getMockedSimMedicalPhysicsConsult();
        when(simMedicalPhysicsConsultRepository.findById(anyLong())).thenReturn(Optional.of(simMedicalPhysicsConsult));
        simMedicalPhysicsConsultService.getSimMedicalPhysicsConsult(SIM_MEDICAL_PHYSICS_CONSULTS_ID);
        verify(simMedicalPhysicsConsultRepository, times(1)).findById(SIM_MEDICAL_PHYSICS_CONSULTS_ID);
    }

    @Test
    void deleteSimMedicalPhysicsConsult() {
        final SimMedicalPhysicsConsult simMedicalPhysicsConsult = getMockedSimMedicalPhysicsConsult();
        when(simMedicalPhysicsConsultRepository.findById(anyLong())).thenReturn(Optional.of(simMedicalPhysicsConsult));
        simMedicalPhysicsConsultService.deleteSimMedicalPhysicsConsult(simMedicalPhysicsConsult.getId());
        verify(simMedicalPhysicsConsultRepository, times(1)).findById(SIM_MEDICAL_PHYSICS_CONSULTS_ID);
    }

    @Test
    void deleteSimMedicalPhysicsConsultWhenThrowException() {
        when(simMedicalPhysicsConsultRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> simMedicalPhysicsConsultService.deleteSimMedicalPhysicsConsult(SIM_MEDICAL_PHYSICS_CONSULTS_ID));
    }

    private SimMedicalPhysicsConsult getMockedSimMedicalPhysicsConsult() {
        final SimMedicalPhysicsConsult simMedicalPhysicsConsult = new SimMedicalPhysicsConsult();
        simMedicalPhysicsConsult.setId(SIM_MEDICAL_PHYSICS_CONSULTS_ID);
        simMedicalPhysicsConsult.setSimOrder(getMockedSimOrder());
        simMedicalPhysicsConsult.setMedicalPhysicsConsult(getMockedMedicalPhysicsConsult());
        simMedicalPhysicsConsult.setAdditionalInstruction("Test Instruction");
        return simMedicalPhysicsConsult;
    }

    private SimOrder getMockedSimOrder() {
        final SimOrder simOrder = new SimOrder();
        simOrder.setId(1L);
        return simOrder;
    }

    private MedicalPhysicsConsult getMockedMedicalPhysicsConsult() {
        final MedicalPhysicsConsult medicalPhysicsConsult = new MedicalPhysicsConsult();
        medicalPhysicsConsult.setId(1L);
        return medicalPhysicsConsult;
    }
}
