package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_WORKFLOW_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_DUPLICATE_CLINIC_WORKFLOW;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_WORKFLOW_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ClinicWorkflow.FK_CLINIC_WORKFLOW_CLINIC_ID;
import static com.test.api.healthcare.configurations.models.entities.ClinicWorkflow.FK_CLINIC_WORKFLOW_WORKFLOW_ID;
import static com.test.api.healthcare.configurations.models.entities.ClinicWorkflow.UK_CLINIC_WORKFLOW;

import com.test.api.healthcare.common.exceptions.DataAlreadyExistsException;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.PageQueryParam;
import com.test.api.healthcare.common.utils.SortUtil;
import com.test.api.healthcare.configurations.constants.WorkflowType;
import com.test.api.healthcare.configurations.models.entities.ClinicWorkflow;
import com.test.api.healthcare.configurations.repositories.ClinicWorkflowRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClinicWorkflowService {

    private final ClinicWorkflowRepository clinicWorkflowRepository;

    public ClinicWorkflow createClinicWorkflow(final ClinicWorkflow clinicWorkflowToCreate) {
        try {
            return clinicWorkflowRepository.save(clinicWorkflowToCreate);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateClinicWorkflowCreationFailure(ex, clinicWorkflowToCreate);
            throw ex;
        }
    }

    private void handleCreateClinicWorkflowCreationFailure(final DataIntegrityViolationException ex,
                                                           final ClinicWorkflow clinicWorkflow) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_CLINIC_WORKFLOW_CLINIC_ID)) {
                throw new InvalidFieldException("clinicWorkflow", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, clinicWorkflow.getClinic().getId()));
            } else if (constraintName.equals(FK_CLINIC_WORKFLOW_WORKFLOW_ID)) {
                throw new InvalidFieldException("clinicWorkflow", "workflowId",
                    String.format(ERR_MSG_WORKFLOW_NOT_FOUND, clinicWorkflow.getWorkflow().getId()));
            } else if (constraintName.equals(UK_CLINIC_WORKFLOW)) {
                throw new DataAlreadyExistsException(ERR_MSG_DUPLICATE_CLINIC_WORKFLOW);
            }
        }
    }

    public Optional<ClinicWorkflow> getClinicWorkflow(final Long clinicWorkflowId) {
        return clinicWorkflowRepository.findById(clinicWorkflowId);
    }

    public Optional<ClinicWorkflow> getClinicWorkflow(final WorkflowType workflowType,
                                                      final int workflowStepSeq,
                                                      final Long clinicId) {
        return clinicWorkflowRepository.findClinicWorkflow(workflowType, workflowStepSeq, clinicId);
    }

    public Page<ClinicWorkflow> listClinicWorkflows(final PageQueryParam pageQueryParam) {
        final Sort sortBy = SortUtil.sort(pageQueryParam.getSortField(), pageQueryParam.getSortDirection());
        final Pageable pageable = PageRequest.of(pageQueryParam.getPageNo(), pageQueryParam.getPageSize(), sortBy);
        return clinicWorkflowRepository.findAll(pageable);
    }

    public void deleteClinicWorkflow(final Long clinicWorkflowId) {
        final ClinicWorkflow clinicWorkflow = clinicWorkflowRepository.findById(clinicWorkflowId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_WORKFLOW_NOT_FOUND, clinicWorkflowId)));
        clinicWorkflowRepository.delete(clinicWorkflow);
    }
}
