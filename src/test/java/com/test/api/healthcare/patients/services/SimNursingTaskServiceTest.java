package com.test.api.healthcare.patients.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.patients.constants.NursingTask;
import com.test.api.healthcare.patients.constants.SimNursingTaskStatus;
import com.test.api.healthcare.patients.models.entities.SimNursingTask;
import com.test.api.healthcare.patients.models.entities.SimOrder;
import com.test.api.healthcare.patients.repositories.SimNursingTaskRepository;

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
class SimNursingTaskServiceTest {

    private static final Long SIM_NURSING_TASK_ID = 1L;

    @InjectMocks
    private SimNursingTaskService simNursingTaskService;

    @Mock
    private SimNursingTaskRepository simNursingTaskRepository;

    private static Stream<Arguments> createSimNursingTaskExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Sim Order Id", "fk_sim_nursing_task_request_sim_order_id")
        );
    }

    @Test
    void createSimNursingTask() {
        final SimNursingTask simNursingTask = getMockedSimNursingTask();
        when(simNursingTaskRepository.save(any())).thenReturn(simNursingTask);
        final SimNursingTask simNursingTaskResponse = simNursingTaskService.createSimNursingTask(simNursingTask);
        Assertions.assertNotNull(simNursingTaskResponse);
    }

    @ParameterizedTest
    @MethodSource("createSimNursingTaskExceptionSource")
    void createSimNursingTaskWhenThrowException(final String message, final String constraintName) {
        final SimNursingTask simNursingTask = getMockedSimNursingTask();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(simNursingTaskRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class, () -> simNursingTaskService.createSimNursingTask(simNursingTask));
    }

    @Test
    void listSimNursingTasks() {
        final List<SimNursingTask> simNursingTasks = List.of(getMockedSimNursingTask());
        when(simNursingTaskRepository.findAll()).thenReturn(simNursingTasks);
        final List<SimNursingTask> simNursingTasksResponses = simNursingTaskService.listSimNursingTasks();
        Assertions.assertEquals(1, simNursingTasksResponses.size());
    }

    @Test
    void deleteSimNursingTask() {
        final SimNursingTask simNursingTask = getMockedSimNursingTask();
        when(simNursingTaskRepository.findById(SIM_NURSING_TASK_ID)).thenReturn(Optional.of(simNursingTask));
        simNursingTaskService.deleteSimNursingTask(simNursingTask.getId());
        verify(simNursingTaskRepository, times(1)).findById(SIM_NURSING_TASK_ID);
    }

    @Test
    void deleteSimNursingTaskWhenThrowException() {
        when(simNursingTaskRepository.findById(SIM_NURSING_TASK_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> simNursingTaskService.deleteSimNursingTask(SIM_NURSING_TASK_ID));
    }

    @Test
    void updateSimNursingTask() {
        final SimNursingTask simNursingTask = getMockedSimNursingTask();
        when(simNursingTaskRepository.findById(anyLong())).thenReturn(Optional.of(simNursingTask));
        when(simNursingTaskRepository.save(any())).thenReturn(simNursingTask);
        final SimNursingTask simNursingTaskResponse = simNursingTaskService.updateSimNursingTask(simNursingTask, SIM_NURSING_TASK_ID);
        Assertions.assertNotNull(simNursingTaskResponse);
    }

    @ParameterizedTest
    @MethodSource("updateSimNursingTaskExceptionSource")
    void updateSimNursingTaskWhenThrowException(final String message, final String constraintName) {
        final SimNursingTask simNursingTask = getMockedSimNursingTask();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(simNursingTaskRepository.findById(anyLong())).thenReturn(Optional.of(simNursingTask));
        when(simNursingTaskRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> simNursingTaskService.updateSimNursingTask(simNursingTask, SIM_NURSING_TASK_ID));
    }

    private static Stream<Arguments> updateSimNursingTaskExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Sim Nursing Task Id", "fk_sim_nursing_task_request_sim_order_id")
        );
    }


    private SimNursingTask getMockedSimNursingTask() {
        final SimNursingTask simNursingTask = new SimNursingTask();
        simNursingTask.setId(SIM_NURSING_TASK_ID);
        simNursingTask.setSimOrder(getMockedSimOrder());
        simNursingTask.setNursingTaskStatus(SimNursingTaskStatus.REQUESTED.name());
        simNursingTask.setNursingTask(NursingTask.CREATININE);
        return simNursingTask;
    }

    private SimOrder getMockedSimOrder() {
        final SimOrder simOrder = new SimOrder();
        simOrder.setId(1L);
        return simOrder;
    }
}

