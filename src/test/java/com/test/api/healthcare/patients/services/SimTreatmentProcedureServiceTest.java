package com.test.api.healthcare.patients.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_SIM_ORDER_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_TREATMENT_PROCEDURE_NOT_FOUND_FOR_ID;
import static com.test.api.healthcare.patients.models.entities.SimTreatmentProcedure.FK_SIM_SPECIAL_TREATMENT_PROCEDURE_SIM_ORDER_ID;
import static com.test.api.healthcare.patients.models.entities.SimTreatmentProcedure.FK_SIM_SPECIAL_TREATMENT_PROCEDURE_TREATMENT_PROCEDURE_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.configurations.models.entities.TreatmentProcedure;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.models.entities.SimTreatmentProcedure;
import com.test.api.healthcare.patients.repositories.SimTreatmentProcedureRepository;

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
class SimTreatmentProcedureServiceTest {

    private static final Long SIM_TREATMENT_PROCEDURE_ID = 1L;

    @InjectMocks
    private SimTreatmentProcedureService simTreatmentProcedureService;

    @Mock
    private SimTreatmentProcedureRepository simTreatmentProcedureRepository;

    @ParameterizedTest
    @MethodSource("createOrUpdateSimTreatmentProcedureExceptionSource")
    void createOrUpdateSimTreatmentProcedureWhenThrowException(final String message, final String constraintName) {
        final SimTreatmentProcedure simTreatmentProcedure = getMockedSimTreatmentProcedure();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(simTreatmentProcedureRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(DataNotFoundException.class,
            () -> simTreatmentProcedureService.createOrUpdateSimTreatmentProcedure(simTreatmentProcedure));
    }

    private static Stream<Arguments> createOrUpdateSimTreatmentProcedureExceptionSource() {
        return Stream.of(
            Arguments.of(ERR_MSG_SIM_ORDER_NOT_FOUND, FK_SIM_SPECIAL_TREATMENT_PROCEDURE_SIM_ORDER_ID),
            Arguments.of(ERR_MSG_TREATMENT_PROCEDURE_NOT_FOUND_FOR_ID, FK_SIM_SPECIAL_TREATMENT_PROCEDURE_TREATMENT_PROCEDURE_ID)
        );
    }

    @Test
    void createOrUpdateSimTreatmentProcedure() {
        final SimTreatmentProcedure simTreatmentProcedure = getMockedSimTreatmentProcedure();
        when(simTreatmentProcedureRepository.save(any())).thenReturn(simTreatmentProcedure);
        final SimTreatmentProcedure simTreatmentProcedureResponse =
            simTreatmentProcedureService.createOrUpdateSimTreatmentProcedure(simTreatmentProcedure);
        Assertions.assertNotNull(simTreatmentProcedureResponse);
    }

    @Test
    void getSimTreatmentProcedure() {
        final SimTreatmentProcedure simTreatmentProcedure = getMockedSimTreatmentProcedure();
        when(simTreatmentProcedureRepository.findById(SIM_TREATMENT_PROCEDURE_ID))
            .thenReturn(Optional.of(simTreatmentProcedure));
        simTreatmentProcedureService.getSimTreatmentProcedure(simTreatmentProcedure.getId());
        verify(simTreatmentProcedureRepository, times(1)).findById(SIM_TREATMENT_PROCEDURE_ID);
    }


    @Test
    void deleteSimTreatmentProcedure() {
        final SimTreatmentProcedure simTreatmentProcedure = getMockedSimTreatmentProcedure();
        simTreatmentProcedureService.deleteSimTreatmentProcedure(simTreatmentProcedure.getId());
        verify(simTreatmentProcedureRepository, times(1)).deleteById(SIM_TREATMENT_PROCEDURE_ID);
    }

    private SimTreatmentProcedure getMockedSimTreatmentProcedure() {
        final SimTreatmentProcedure simTreatmentProcedure = new SimTreatmentProcedure();
        simTreatmentProcedure.setId(SIM_TREATMENT_PROCEDURE_ID);
        simTreatmentProcedure.setSimOrder(getMockedSimOrder());
        simTreatmentProcedure.setTreatmentProcedure(getMockedTreatmentProcedure());
        simTreatmentProcedure.setAdditionalInstruction("Test 1");
        return simTreatmentProcedure;
    }

    private SimOrder getMockedSimOrder() {
        final SimOrder simOrder = new SimOrder();
        simOrder.setId(1L);
        return simOrder;
    }

    private TreatmentProcedure getMockedTreatmentProcedure() {
        final TreatmentProcedure treatmentProcedure = new TreatmentProcedure();
        treatmentProcedure.setId(1L);
        return treatmentProcedure;
    }
}
