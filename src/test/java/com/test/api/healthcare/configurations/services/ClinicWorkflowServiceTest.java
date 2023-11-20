package com.test.api.healthcare.configurations.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.PageQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.ClinicWorkflow;
import com.test.api.healthcare.configurations.models.entities.Workflow;
import com.test.api.healthcare.configurations.repositories.ClinicWorkflowRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
class ClinicWorkflowServiceTest {

    private static final Long CLINIC_WORKFLOW_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final Long WORK_FLOW_ID = 1L;
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    private ClinicWorkflowRepository clinicWorkflowRepository;

    @InjectMocks
    private ClinicWorkflowService clinicWorkflowService;

    private static Stream<Arguments> createClinicWorkflowExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_clinic_workflow_clinic_id"),
            Arguments.of("Invalid WorkFlow Id", "fk_clinic_workflow_workflow_id")
        );
    }

    @Test
    void createClinicWorkflow() {
        final ClinicWorkflow clinicWorkflow = getMockedClinicWorkflow();
        when(clinicWorkflowRepository.save(any())).thenReturn(getMockedClinicWorkflow());
        final ClinicWorkflow response = clinicWorkflowService.createClinicWorkflow(clinicWorkflow);
        assertNotNull(response);
    }

    @ParameterizedTest
    @MethodSource("createClinicWorkflowExceptionSource")
    void createClinicWorkflowWhenThrowException(final String message, final String constraintName) {
        final ClinicWorkflow clinicWorkflow = getMockedClinicWorkflow();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(clinicWorkflowRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> clinicWorkflowService.createClinicWorkflow(clinicWorkflow));
    }

    @Test
    void getClinicWorkflow() {
        final ClinicWorkflow clinicWorkflow = getMockedClinicWorkflow();
        when(clinicWorkflowRepository.findById(clinicWorkflow.getId())).thenReturn(Optional.of(clinicWorkflow));
        final Optional<ClinicWorkflow> response = clinicWorkflowService.getClinicWorkflow(clinicWorkflow.getId());
        assertTrue(response.isPresent());
    }

    @Test
    void deleteClinicWorkflow() {
        final ClinicWorkflow clinicWorkflow = getMockedClinicWorkflow();
        when(clinicWorkflowRepository.findById(clinicWorkflow.getId())).thenReturn(Optional.of(clinicWorkflow));
        clinicWorkflowService.deleteClinicWorkflow(clinicWorkflow.getId());
        verify(clinicWorkflowRepository, times(1)).findById(CLINIC_WORKFLOW_ID);
    }

    @Test
    void listClinics() {
        final List<ClinicWorkflow> clinicWorkflowList = listMockedClinicWorkflows();
        final PageQueryParam pageQueryParam = PageQueryParam.builder()
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("ASC")
            .build();
        final Page<ClinicWorkflow> expectedPage = new PageImpl<>(clinicWorkflowList);
        when(clinicWorkflowRepository.findAll(any(Pageable.class)))
            .thenReturn(expectedPage);

        final Page<ClinicWorkflow> resultPage = clinicWorkflowService.listClinicWorkflows(pageQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void listClinicsWithDescOrder() {
        final List<ClinicWorkflow> clinicWorkflowList = listMockedClinicWorkflows();
        final PageQueryParam pageQueryParam = PageQueryParam.builder()
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("DESC")
            .build();
        final Page<ClinicWorkflow> expectedPage = new PageImpl<>(clinicWorkflowList);
        when(clinicWorkflowRepository.findAll(any(Pageable.class)))
            .thenReturn(expectedPage);

        final Page<ClinicWorkflow> resultPage = clinicWorkflowService.listClinicWorkflows(pageQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    private ClinicWorkflow getMockedClinicWorkflow() {
        final ClinicWorkflow clinicWorkflow = new ClinicWorkflow();
        clinicWorkflow.setId(CLINIC_WORKFLOW_ID);
        clinicWorkflow.setClinic(getMockedClinic());
        clinicWorkflow.setWorkflow(getMockedWorkflow());
        return clinicWorkflow;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private Workflow getMockedWorkflow() {
        final Workflow workflow = new Workflow();
        workflow.setId(WORK_FLOW_ID);
        return workflow;
    }

    private List<ClinicWorkflow> listMockedClinicWorkflows() {
        return List.of(getMockedClinicWorkflow());
    }

}
