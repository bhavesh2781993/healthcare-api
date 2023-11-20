package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.FusionQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.Fusion;
import com.test.api.healthcare.configurations.repositories.FusionRepository;

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
class FusionServiceTest {

    private static final Long FUSION_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String FUSION = "fusion 1";

    @InjectMocks
    FusionService fusionService;

    @Mock
    FusionRepository fusionRepository;

    private static Stream<Arguments> createFusionExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_fusion_lookup_clinic_id")
        );
    }

    @Test
    void createFusion() {
        final Fusion fusion = getMockFusion();
        when(fusionRepository.save(any())).thenReturn(fusion);
        final Fusion createFusionResponse = fusionService.createFusion(fusion);
        Assertions.assertNotNull(createFusionResponse);
    }

    @ParameterizedTest
    @MethodSource("createFusionExceptionSource")
    void createFusionWhenThrowException(final String message, final String constraintName) {
        final Fusion fusion = getMockFusion();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(fusionRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> fusionService.createFusion(fusion));
    }

    @Test
    void listFusion() {
        final List<Fusion> fusions = getFusionList();
        final FusionQueryParam fusionQueryParam = FusionQueryParam.builder()
            .clinicId(CLINIC_ID)
            .build();
        when(fusionRepository.findAllByClinicId(fusionQueryParam.getClinicId()))
            .thenReturn(fusions);
        final List<Fusion> fusionListResponse = fusionService.listFusion(fusionQueryParam);
        Assertions.assertEquals(1, fusionListResponse.size());
    }

    @Test
    void deleteFusion() {
        final Fusion fusion = new Fusion();
        when(fusionRepository.findByIdAndClinicId(fusion.getId(), CLINIC_ID))
            .thenReturn(Optional.of(fusion));
        fusionService.deleteFusion(fusion.getId(), CLINIC_ID);
        verify(fusionRepository, times(1)).findByIdAndClinicId(fusion.getId(), CLINIC_ID);
    }

    @Test
    void deleteFusionWhenReturnNull() {
        final Fusion fusion = getMockFusion();
        when(fusionRepository.findByIdAndClinicId(fusion.getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class,
            () -> fusionService.deleteFusion(FUSION_ID, CLINIC_ID));
    }

    private Fusion getMockFusion() {
        final Fusion fusion = new Fusion();
        fusion.setId(FUSION_ID);
        fusion.setClinic(getMockedClinic());
        fusion.setFusions(FUSION);
        return fusion;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<Fusion> getFusionList() {
        return List.of(getMockFusion());
    }
}
