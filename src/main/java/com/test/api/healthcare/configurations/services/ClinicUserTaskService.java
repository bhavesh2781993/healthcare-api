package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_USER_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_USER_TASK_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_PATIENT_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ClinicUserTask.FK_CLINIC_USER_TASK_CLINIC_ID;
import static com.test.api.healthcare.configurations.models.entities.ClinicUserTask.FK_CLINIC_USER_TASK_PATIENT_ID;
import static com.test.api.healthcare.configurations.models.entities.ClinicUserTask.FK_CLINIC_USER_TASK_TASK_ASSIGNED_TO_ID;
import static io.github.perplexhub.rsql.RSQLJPASupport.toSort;
import static io.github.perplexhub.rsql.RSQLJPASupport.toSpecification;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.FiqlQueryParam;
import com.test.api.healthcare.configurations.models.entities.ClinicUserTask;
import com.test.api.healthcare.configurations.repositories.ClinicUserTaskRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClinicUserTaskService {

    private final ClinicUserTaskRepository clinicUserTaskRepository;

    public ClinicUserTask createClinicUserTask(final ClinicUserTask clinicUserTask) {
        try {
            return clinicUserTaskRepository.save(clinicUserTask);
        } catch (final DataIntegrityViolationException ex) {
            handleClinicUserTaskFailure(ex, clinicUserTask);
            throw ex;
        }
    }

    public ClinicUserTask updateClinicUserTask(final ClinicUserTask clinicUserTask) {
        if (isClinicUserTaskNotPresent(clinicUserTask.getId())) {
            throw new DataNotFoundException(String.format(ERR_MSG_CLINIC_USER_TASK_NOT_FOUND, clinicUserTask.getId()));
        }

        try {
            return clinicUserTaskRepository.save(clinicUserTask);
        } catch (final DataIntegrityViolationException ex) {
            handleClinicUserTaskFailure(ex, clinicUserTask);
            throw ex;
        }
    }

    private void handleClinicUserTaskFailure(final DataIntegrityViolationException ex,
                                             final ClinicUserTask clinicUserTask) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_CLINIC_USER_TASK_PATIENT_ID)) {
                throw new InvalidFieldException("clinicUserTask", "patientId",
                    String.format(ERR_MSG_PATIENT_NOT_FOUND, clinicUserTask.getPatient().getId()));
            }
            if (constraintName.equals(FK_CLINIC_USER_TASK_TASK_ASSIGNED_TO_ID)) {
                throw new InvalidFieldException("clinicUserTask", "taskAssignedTo",
                    String.format(ERR_MSG_CLINIC_USER_NOT_FOUND, clinicUserTask.getTaskAssignedTo().getId()));
            }
            if (constraintName.equals(FK_CLINIC_USER_TASK_CLINIC_ID)) {
                throw new InvalidFieldException("clinicUserTask", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, clinicUserTask.getClinic().getId()));
            }
        }
    }

    public Optional<ClinicUserTask> getClinicUserTask(final Long clinicUserTaskId) {
        return clinicUserTaskRepository.findClinicUserTaskAndPatient(clinicUserTaskId);
    }

    public boolean isClinicUserTaskNotPresent(final Long clinicUserTaskId) {
        return !clinicUserTaskRepository.existsById(clinicUserTaskId);
    }

    public Page<ClinicUserTask> listClinicUserTasks(final FiqlQueryParam queryParam) {
        final Specification<ClinicUserTask> eagerFetchPatient = ClinicUserTaskRepository.Specs.eagerFetchPatient();
        final Specification<ClinicUserTask> filterSpec = toSpecification(queryParam.getFilter());
        final Specification<ClinicUserTask> sortSpec = toSort(queryParam.getSort());
        final Specification<ClinicUserTask> filterAndQuerySpec = Specification.allOf(eagerFetchPatient, filterSpec, sortSpec);
        final Pageable pageable = PageRequest.of(queryParam.getPageNo(), queryParam.getPageSize());

        return clinicUserTaskRepository.findAll(filterAndQuerySpec, pageable);
    }

    public void deleteClinicUserTask(final Long clinicUserTaskId) {
        if (isClinicUserTaskNotPresent(clinicUserTaskId)) {
            throw new DataNotFoundException(String.format(ERR_MSG_CLINIC_USER_TASK_NOT_FOUND, clinicUserTaskId));
        }
        clinicUserTaskRepository.deleteById(clinicUserTaskId);
    }

}
