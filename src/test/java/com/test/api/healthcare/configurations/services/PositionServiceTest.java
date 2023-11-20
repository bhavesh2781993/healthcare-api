package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.PositionQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.Position;
import com.test.api.healthcare.configurations.repositories.PositionRepository;

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
class PositionServiceTest {

    private static final Long POSITION_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String POSITION_TYPE = "ORM";
    private static final String POSITION = "Position 1";


    @InjectMocks
    PositionService positionService;

    @Mock
    PositionRepository positionRepository;

    private static Stream<Arguments> createPositionExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_position_lookup_clinic_id")
        );
    }

    @Test
    void createPosition() {
        final Position position = getMockPosition();
        when(positionRepository.save(any())).thenReturn(position);
        final Position positionResponse = positionService.createPosition(position);
        Assertions.assertNotNull(positionResponse);
    }

    @ParameterizedTest
    @MethodSource("createPositionExceptionSource")
    void createPositionWhenThrowException(final String message, final String constraintName) {
        final Position position = getMockPosition();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(positionRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> positionService.createPosition(position));
    }

    @Test
    void listPositions() {
        final List<Position> position = getMockPositionList();
        final PositionQueryParam positionQueryParam = PositionQueryParam.builder()
            .positionType(POSITION_TYPE)
            .clinicId(CLINIC_ID)
            .build();
        when(positionRepository
            .findAllByPositionTypeAndClinicId(positionQueryParam.getPositionType(), positionQueryParam.getClinicId()))
            .thenReturn(position);
        final List<Position> positionListResponse = positionService.listPositions(positionQueryParam);
        Assertions.assertEquals(1, positionListResponse.size());
    }

    @Test
    void deletePosition() {
        final Position position = getMockPosition();
        when(positionRepository.findByIdAndClinicId(POSITION_ID, CLINIC_ID)).thenReturn(Optional.of(position));
        positionService.deletePosition(position.getId(), CLINIC_ID);
        verify(positionRepository, times(1)).findByIdAndClinicId(POSITION_ID, CLINIC_ID);
    }

    @Test
    void deletePositionWhenThrowException() {
        when(positionRepository.findByIdAndClinicId(POSITION_ID, CLINIC_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> positionService.deletePosition(POSITION_ID, CLINIC_ID));
    }

    private Position getMockPosition() {
        final Position position = new Position();
        position.setId(POSITION_ID);
        position.setClinic(getMockedClinic());
        position.setPositionType(POSITION_TYPE);
        position.setPosition(POSITION);
        return position;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<Position> getMockPositionList() {
        return List.of(getMockPosition());
    }
}
