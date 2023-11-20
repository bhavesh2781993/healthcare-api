package com.test.api.healthcare.configurations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.models.PageQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.repositories.ClinicRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    private static final Long CLINIC_ID = 1L;
    private static final String CLINIC_NAME = "test clinic";
    private static final int PAGE_NO = 1;
    private static final int PAGE_SIZE = 10;

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicService clinicService;

    @Test
    void createClinic() {
        final Clinic clinic = getMockedClinic();
        when(clinicRepository.save(any())).thenReturn(getMockedClinic());
        final Clinic clinicResponse = clinicService.createClinic(clinic);
        Assertions.assertNotNull(clinicResponse);
    }

    @Test
    void getClinic() {
        final Clinic clinic = getMockedClinic();
        when(clinicRepository.findById(clinic.getId())).thenReturn(Optional.of(clinic));
        final Optional<Clinic> clinicResponse = clinicService.getClinic(clinic.getId());
        assertTrue(clinicResponse.isPresent());
    }

    @Test
    void deleteClinic() {
        final Clinic clinic = getMockedClinic();
        when(clinicRepository.findById(clinic.getId())).thenReturn(Optional.of(clinic));
        clinicService.deleteClinic(clinic.getId());
        verify(clinicRepository, times(1)).findById(CLINIC_ID);
    }

    @Test
    void deleteClinicWhenReturnNull() {
        final Clinic clinic = getMockedClinic();
        when(clinicRepository.findById(clinic.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> clinicService.deleteClinic(CLINIC_ID));
    }

    @Test
    void listClinics() {
        final List<Clinic> clinicList = listMockedClinics();
        final PageQueryParam pageQueryParam;
        pageQueryParam = PageQueryParam.builder()
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("ASC")
            .build();
        final Page<Clinic> expectedPage = new PageImpl<>(clinicList);
        when(clinicRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPage);

        final Page<Clinic> resultPage = clinicService.listClinics(pageQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void listClinicsWhenDiscOrder() {
        final List<Clinic> clinicList = listMockedClinics();
        final PageQueryParam pageQueryParam = PageQueryParam.builder()
            .pageNo(PAGE_NO)
            .pageSize(PAGE_SIZE)
            .sortField("name")
            .sortDirection("DESC")
            .build();
        final Page<Clinic> expectedPage = new PageImpl<>(clinicList);
        when(clinicRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(expectedPage);

        final Page<Clinic> resultPage = clinicService.listClinics(pageQueryParam);

        assertEquals(1, resultPage.getTotalElements());
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        clinic.setName(CLINIC_NAME);
        return clinic;
    }

    private List<Clinic> listMockedClinics() {
        return List.of(getMockedClinic());
    }
}