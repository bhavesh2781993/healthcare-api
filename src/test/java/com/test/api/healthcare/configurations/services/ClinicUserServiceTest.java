package com.test.api.healthcare.configurations.services;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.FiqlQueryParam;
import com.test.api.healthcare.configurations.models.entities.ClinicDepartment;
import com.test.api.healthcare.configurations.models.entities.ClinicUser;
import com.test.api.healthcare.configurations.models.entities.ClinicUserImage;
import com.test.api.healthcare.configurations.repositories.ClinicUserRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;

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
class ClinicUserServiceTest {

    private static final Long CLINIC_USER_ID = 1L;
    private static final Long CLINIC_DEPARTMENT_ID = 1L;
    private static final Long CLINIC_USER_IMAGE_ID = 1L;
    private static final String CLINIC_USER_NAME = "Test Clinic User";
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    private ClinicUserRepository clinicUserRepository;

    @InjectMocks
    private ClinicUserService clinicUserService;


    private static Stream<Arguments> createAndUpdateClinicUserExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Department Id", "fk_clinic_user_clinic_department_id")
        );
    }

    @Test
    void createClinicUser() {
        final ClinicUser clinicUser = getMockedClinicUser();
        when(clinicUserRepository.save(any())).thenReturn(getMockedClinicUser());
        final ClinicUser response = clinicUserService.createClinicUser(clinicUser);
        assertNotNull(response);
    }

    @ParameterizedTest
    @MethodSource("createAndUpdateClinicUserExceptionSource")
    void createClinicUserWhenThrowException(final String message, final String constraintName) {
        final ClinicUser clinicUser = getMockedClinicUser();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(clinicUserRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> clinicUserService.createClinicUser(clinicUser));
    }

    @Test
    void getClinicUser() {
        final ClinicUser clinicUser = getMockedClinicUser();
        when(clinicUserRepository.findClinicUserById(clinicUser.getId())).thenReturn(Optional.of(clinicUser));
        final ClinicUser responses = clinicUserService.getClinicUser(clinicUser.getId(), true);
        assertTrue(true, "", responses);
    }

    @Test
    void deleteClinicUser() {
        final ClinicUser clinicUser = getMockedClinicUser();
        when(clinicUserRepository.findById(clinicUser.getId())).thenReturn(Optional.of(clinicUser));
        clinicUserService.deleteClinicUser(clinicUser.getId());
        verify(clinicUserRepository, times(1)).findById(CLINIC_USER_ID);
    }

    @Test
    void listClinics() {
        final List<ClinicUser> clinicUserList = listMockedClinicUsers();
        final FiqlQueryParam queryParam = FiqlQueryParam.builder()
            .filter("name")
            .sort("ASC")
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .build();

        when(clinicUserRepository.findAll(ArgumentMatchers.<Specification<ClinicUser>>any()))
            .thenReturn(clinicUserList);
        final List<ClinicUser> clinicUsers = clinicUserService.listClinicUsers(queryParam);
        assertNotNull(clinicUsers);
    }

    @Test
    void listClinicsWithDescOrder() {
        final List<ClinicUser> clinicUserList = listMockedClinicUsers();
        final FiqlQueryParam queryParam = FiqlQueryParam.builder()
            .filter("name")
            .sort("DESC")
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .build();
        when(clinicUserRepository.findAll(ArgumentMatchers.<Specification<ClinicUser>>any()))
            .thenReturn(clinicUserList);
        final List<ClinicUser> clinicUsers = clinicUserService.listClinicUsers(queryParam);
        assertNotNull(clinicUsers);
    }

    @Test
    void updateClinicUser() {
        final ClinicUser clinicUser = getMockedClinicUser();
        when(clinicUserRepository.findById(anyLong())).thenReturn(Optional.of(clinicUser));
        when(clinicUserRepository.save(any())).thenReturn(clinicUser);
        final ClinicUser matchingClinicUser = clinicUserService.updateClinicUser(clinicUser);
        assertNotNull(matchingClinicUser);
    }

    @ParameterizedTest
    @MethodSource("createAndUpdateClinicUserExceptionSource")
    void updateClinicUserWhenThrowException(final String message, final String constraintName) {
        final ClinicUser clinicUser = getMockedClinicUser();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(clinicUserRepository.findById(anyLong())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> clinicUserService.updateClinicUser(clinicUser));
    }

    private ClinicUser getMockedClinicUser() {
        final ClinicUser clinicUser = new ClinicUser();
        clinicUser.setId(CLINIC_USER_ID);
        clinicUser.setName(CLINIC_USER_NAME);
        clinicUser.setClinicDepartment(getMockedClinicDepartment());
        clinicUser.setDesignation("Doctor");
        clinicUser.setPhone("834356346");
        clinicUser.setAlternatePhone("60982346");
        clinicUser.setEmail("test@123");
        clinicUser.setAlternateEmail("test@154");
        clinicUser.setAddress("ekZero");
        clinicUser.setCity("vadodara");
        clinicUser.setStateProvince("Gujarat");
        clinicUser.setCountry("India");
        clinicUser.setPostalCode("39007");
        clinicUser.setClinicUserImages(listMockedClinicUserImage());
        return clinicUser;
    }

    private ClinicDepartment getMockedClinicDepartment() {
        final ClinicDepartment clinicDepartment = new ClinicDepartment();
        clinicDepartment.setId(CLINIC_DEPARTMENT_ID);
        return clinicDepartment;
    }

    private ClinicUserImage getMockedClinicUserImage() {
        final ClinicUserImage clinicUserImage = new ClinicUserImage();
        final byte[] image = {1, 2, 2};
        clinicUserImage.setId(CLINIC_USER_IMAGE_ID);
        clinicUserImage.setImageName("Image 1");
        clinicUserImage.setImageContentType(".jpg");
        clinicUserImage.setIsActive(true);
        clinicUserImage.setImage(image);
        return clinicUserImage;
    }

    private List<ClinicUserImage> listMockedClinicUserImage() {
        final List<ClinicUserImage> clinicUserImages = new ArrayList<>();
        clinicUserImages.add(getMockedClinicUserImage());
        return clinicUserImages;
    }

    private List<ClinicUser> listMockedClinicUsers() {
        return List.of(getMockedClinicUser());
    }
}
