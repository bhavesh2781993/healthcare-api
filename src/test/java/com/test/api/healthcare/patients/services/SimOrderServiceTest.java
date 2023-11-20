package com.test.api.healthcare.patients.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.PageQueryParam;
import com.test.api.healthcare.configurations.models.entities.Position;
import com.test.api.healthcare.patients.constants.SimOrderStatus;
import com.test.api.healthcare.patients.models.CtScanInstruction;
import com.test.api.healthcare.patients.models.entities.PatientTracker;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.models.entities.SimOrderDevice;
import com.test.api.healthcare.patients.models.entities.SimOrderInstruction;
import com.test.api.healthcare.patients.repositories.SimOrderRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimOrderServiceTest {

    private static final Long SIM_ORDER_ID = 1L;
    private static final Long PATIENT_TRACKER_ID = 1L;
    private static final String ISOLATION_LOCATION = "vadodara";
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    SimOrderRepository simOrderRepository;

    @InjectMocks
    SimOrderService simOrderService;

    @Test
    void createSimOrder() {
        final SimOrder simOrder = getMockedSimOrder();
        when(simOrderRepository.save(any()))
            .thenReturn(simOrder);
        final SimOrder simOrderResponse = simOrderService.createOrUpdateSimOrder(simOrder);
        Assertions.assertNotNull(simOrderResponse);
    }

    @ParameterizedTest
    @MethodSource("createSimOrderExceptionSource")
    void createSimOrderWhenThrowException(final String message, final String constraintName) {
        final SimOrder simOrder = getMockedSimOrder();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(simOrderRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class, () -> simOrderService.createOrUpdateSimOrder(simOrder));
    }

    private static Stream<Arguments> createSimOrderExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Patient Position", "fk_sim_order_patient_position"),
            Arguments.of("Invalid Arm Position", "fk_sim_order_arm_position"),
            Arguments.of("Invalid Leg Position", "fk_sim_order_leg_position"),
            Arguments.of("Invalid Immobilization Device", "fk_sim_order_device_immobilization_device_lookup_id"),
            Arguments.of("Invalid Additional Instruction", "fk_sim_order_instruction_instruction_lookup_id")
        );
    }

    @Test
    void getSimOrder() {
        final SimOrder simOrder = getMockedSimOrder();
        when(simOrderRepository.findById(SIM_ORDER_ID))
            .thenReturn(Optional.of(simOrder));
        final Optional<SimOrder> simOrderResponse = simOrderService.getSimOrder(simOrder.getId());
        Assertions.assertTrue(simOrderResponse.isPresent());
    }

    @Test
    void listSimOrders() {
        final List<SimOrder> simOrders = listGetMockedSimOrders();
        final PageQueryParam pageQueryParam;

        pageQueryParam = PageQueryParam.builder()
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("simOrderStatus")
            .sortDirection("ASC")
            .build();
        final Page<SimOrder> expectedPage = new PageImpl<>(simOrders);
        when(simOrderRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPage);
        final Page<SimOrder> resultPage = simOrderService.listSimOrders(pageQueryParam);
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void listSimOrdersWhenDiscOrder() {
        final List<SimOrder> simOrders = listGetMockedSimOrders();
        final PageQueryParam pageQueryParam;

        pageQueryParam = PageQueryParam.builder()
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("simOrderStatus")
            .sortDirection("DESC")
            .build();
        final Page<SimOrder> expectedPage = new PageImpl<>(simOrders);
        when(simOrderRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPage);
        final Page<SimOrder> resultPage = simOrderService.listSimOrders(pageQueryParam);
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void deleteSimOrder() {
        final SimOrder simOrder = getMockedSimOrder();
        when(simOrderRepository.findById(any())).thenReturn(Optional.of(simOrder));
        simOrderService.deleteSimOrder(simOrder.getId());
        verify(simOrderRepository, times(1)).findById(SIM_ORDER_ID);
    }

    @Test
    void deleteSimOrderWhenThrowException() {
        when(simOrderRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> simOrderService.deleteSimOrder(SIM_ORDER_ID));
    }

    private static Stream<Arguments> updateSimOrderExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Patient Position", "fk_sim_order_patient_position"),
            Arguments.of("Invalid Arm Position", "fk_sim_order_arm_position"),
            Arguments.of("Invalid Leg Position", "fk_sim_order_leg_position"),
            Arguments.of("Invalid Immobilization Device", "fk_sim_order_device_immobilization_device_lookup_id"),
            Arguments.of("Invalid Additional Instruction", "fk_sim_order_instruction_instruction_lookup_id")
        );
    }

    private List<SimOrder> listGetMockedSimOrders() {
        return  List.of(getMockedSimOrder());
    }

    private SimOrder getMockedSimOrder() {
        final SimOrder simOrder = new SimOrder();
        final CtScanInstruction ctScanInstruction = new CtScanInstruction();
        ctScanInstruction.setFrom("head");
        ctScanInstruction.setTo("leg");

        simOrder.setId(SIM_ORDER_ID);
        simOrder.setPatientTracker(getMockedPatientTracker());
        simOrder.setPatientPosition(getMockedPositionLookup());
        simOrder.setArmPosition(getMockedPositionLookup());
        simOrder.setLegPosition(getMockedPositionLookup());
        simOrder.setPatientPosition(getMockedPositionLookup());
        simOrder.setSimOrderStatus(SimOrderStatus.COMPLETED);
        simOrder.setIsocenterLocation(ISOLATION_LOCATION);
        simOrder.setCtScanInstruction(ctScanInstruction);
        simOrder.setSimOrderDevices(getMockedSimOrderDevice());
        simOrder.setInstructions(getMockedSimOrderInstruction());
        return simOrder;
    }

    private PatientTracker getMockedPatientTracker() {
        final PatientTracker patientTracker = new PatientTracker();
        patientTracker.setId(PATIENT_TRACKER_ID);
        return patientTracker;
    }

    private Position getMockedPositionLookup() {
        final Position position = new Position();
        position.setPosition("TEST");
        return position;
    }

    private List<SimOrderDevice> getMockedSimOrderDevice() {
        return new ArrayList<>();
    }

    private List<SimOrderInstruction> getMockedSimOrderInstruction() {
        return new ArrayList<>();
    }
}
