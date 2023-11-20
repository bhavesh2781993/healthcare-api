package com.test.api.healthcare.patients.services;

import static com.test.api.healthcare.patients.models.entities.SimFusion.FK_SIM_FUSION_SCAN_ID;
import static com.test.api.healthcare.patients.models.entities.SimFusion.FK_SIM_FUSION_SIM_ORDER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.configurations.models.entities.Fusion;
import com.test.api.healthcare.patients.models.entities.SimFusion;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.repositories.SimFusionRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
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
class SimFusionServiceTest {

    private static final Long SIM_FUSION_ID = 1L;
    private static final Long SIM_ORDER_ID = 1L;

    @InjectMocks
    SimFusionService simFusionService;

    @Mock
    SimFusionRepository simFusionRepository;

    private static Stream<Arguments> createSimFusionExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Sim Order Id", FK_SIM_FUSION_SIM_ORDER_ID),
            Arguments.of("Invalid Sim Scan Id", FK_SIM_FUSION_SCAN_ID)
        );
    }

    private static Stream<Arguments> updateSimFusionExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Sim Order Id", FK_SIM_FUSION_SIM_ORDER_ID),
            Arguments.of("Invalid Sim Scan Id", FK_SIM_FUSION_SCAN_ID)
        );
    }

    @Test
    void createSimFusion() {
        final SimFusion simFusion = getMockedSimFusion();
        when(simFusionRepository.save(simFusion)).thenReturn(simFusion);
        final SimFusion simFusionResponse = simFusionService.createSimFusion(simFusion);
        Assertions.assertNotNull(simFusionResponse);
    }

    @ParameterizedTest
    @MethodSource("createSimFusionExceptionSource")
    void createSimFusionWhenThrowException(final String message, final String constraintName) {
        final SimFusion simFusion = getMockedSimFusion();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(simFusionRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class, () -> simFusionService.createSimFusion(simFusion));
    }

    @Test
    void listSimFusions() {
        final List<SimFusion> simFusions = List.of(getMockedSimFusion());
        when(simFusionRepository.findBySimOrderId(anyLong())).thenReturn(simFusions);
        final List<SimFusion> simFusionsResponses = simFusionService.listSimFusions(SIM_ORDER_ID);
        Assertions.assertEquals(1, simFusionsResponses.size());
    }

    @Test
    void deleteSimFusion() {
        final SimFusion simFusion = getMockedSimFusion();
        when(simFusionRepository.findById(simFusion.getId())).thenReturn(Optional.of(simFusion));
        simFusionService.deleteSimFusion(simFusion.getId());
        verify(simFusionRepository, times(1)).findById(simFusion.getId());
    }

    @Test
    void deleteSimOrderWhenReturnNull() {
        when(simFusionRepository.findById(SIM_FUSION_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataValidationException.class,
            () -> simFusionService.deleteSimFusion(SIM_FUSION_ID));
    }

    @Test
    void updateSimFusion() {
        final SimFusion simFusion = getMockedSimFusion();
        when(simFusionRepository.findById(SIM_FUSION_ID)).thenReturn(Optional.of(simFusion));
        when(simFusionRepository.save(any())).thenReturn(simFusion);
        final SimFusion simFusionResponse = simFusionService.updateSimFusion(simFusion, SIM_FUSION_ID);
        Assertions.assertNotNull(simFusionResponse);
    }

    @ParameterizedTest
    @MethodSource("updateSimFusionExceptionSource")
    void updateSimFusionWhenThrowException(final String message, final String constraintName) {
        final SimFusion simFusion = getMockedSimFusion();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(simFusionRepository.findById(anyLong())).thenReturn(Optional.of(simFusion));
        when(simFusionRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> simFusionService.updateSimFusion(simFusion, SIM_FUSION_ID));
    }

    private SimFusion getMockedSimFusion() {
        final LocalDate scanDate = LocalDate.of(2022, 01, 07);
        final SimOrder simOrder = new SimOrder();
        simOrder.setId(1L);
        final SimFusion simFusion = new SimFusion();
        simFusion.setId(SIM_FUSION_ID);
        simFusion.setSimOrder(simOrder);
        simFusion.setScanDate(scanDate);
        simFusion.setScan(getMockedFusion());
        simFusion.setImageSeq("T1");
        return simFusion;
    }

    private Fusion getMockedFusion() {
        final Fusion fusion = new Fusion();
        fusion.setFusions("PET");
        return fusion;
    }
}
