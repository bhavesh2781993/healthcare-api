package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_DEPARTMENT_NOT_FOUND;
import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_USER_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.ClinicUser.FK_CLINIC_USER_CLINIC_DEPARTMENT_ID;
import static io.github.perplexhub.rsql.RSQLJPASupport.toSort;
import static io.github.perplexhub.rsql.RSQLJPASupport.toSpecification;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.FiqlQueryParam;
import com.test.api.healthcare.configurations.models.entities.ClinicUser;
import com.test.api.healthcare.configurations.repositories.ClinicUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClinicUserService {

    private final ClinicUserRepository clinicUserRepository;

    public ClinicUser createClinicUser(final ClinicUser clinicUserToCreate) {
        try {
            clinicUserToCreate.addClinicUserImages();
            return clinicUserRepository.save(clinicUserToCreate);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateClinicUserCreationFailure(ex, clinicUserToCreate);
            throw ex;
        }
    }

    private void handleCreateClinicUserCreationFailure(final DataIntegrityViolationException ex,
                                                       final ClinicUser clinicUser) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_CLINIC_USER_CLINIC_DEPARTMENT_ID)) {
                throw new InvalidFieldException("clinicUser", "clinicDepartmentId",
                    String.format(ERR_MSG_CLINIC_DEPARTMENT_NOT_FOUND, clinicUser.getClinicDepartment().getId()));
            }
        }
    }

    @Transactional(readOnly = true)
    public ClinicUser getClinicUser(final Long clinicUserId, final boolean includeImage) {
        return includeImage ? getClinicUserWithImage(clinicUserId) : getClinicUserWithoutImage(clinicUserId);
    }

    private ClinicUser getClinicUserWithoutImage(final Long clinicUserId) {
        final ClinicUser matchingClinicUser = clinicUserRepository.findById(clinicUserId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_USER_NOT_FOUND, clinicUserId)));
        matchingClinicUser.setClinicUserImages(Collections.emptyList());
        return matchingClinicUser;
    }

    public ClinicUser getClinicUserWithImage(final Long clinicUserId) {
        return clinicUserRepository.findClinicUserById(clinicUserId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_USER_NOT_FOUND, clinicUserId)));
    }

    @Transactional(readOnly = true)
    public List<ClinicUser> listClinicUsers(final FiqlQueryParam queryParam) {
        final Specification<ClinicUser> filterSpec = toSpecification(queryParam.getFilter());
        final Specification<ClinicUser> sortSpec = toSort(queryParam.getSort());
        final Specification<ClinicUser> filterAndQuerySpec = Specification.allOf(filterSpec, sortSpec);

        return clinicUserRepository.findAll(filterAndQuerySpec);
    }

    public void deleteClinicUser(final Long clinicUserId) {
        final ClinicUser clinicUser = clinicUserRepository.findById(clinicUserId)
            .orElseThrow(() -> new DataNotFoundException(String.format(ERR_MSG_CLINIC_USER_NOT_FOUND, clinicUserId)));
        clinicUserRepository.delete(clinicUser);
    }

    public ClinicUser updateClinicUser(final ClinicUser clinicUser) {
        try {
            final ClinicUser matchingClinicUser = clinicUserRepository.findById(clinicUser.getId())
                .orElseThrow(() -> new DataNotFoundException(
                    String.format(ERR_MSG_CLINIC_USER_NOT_FOUND, clinicUser.getId())));
            matchingClinicUser.getClinicUserImages().forEach(clinicUserImage -> clinicUserImage.setIsActive(false));

            clinicUser.addClinicUserImages(matchingClinicUser.getClinicUserImages());
            return clinicUserRepository.save(clinicUser);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateClinicUserCreationFailure(ex, clinicUser);
            throw ex;
        }
    }
}
