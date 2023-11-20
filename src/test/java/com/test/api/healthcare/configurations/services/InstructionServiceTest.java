package com.test.api.healthcare.configurations.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.InstructionQueryParam;
import com.test.api.healthcare.configurations.models.entities.Clinic;
import com.test.api.healthcare.configurations.models.entities.Instruction;
import com.test.api.healthcare.configurations.repositories.InstructionRepository;

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
class InstructionServiceTest {

    private static final Long INSTRUCTION_ID = 1L;
    private static final Long CLINIC_ID = 1L;
    private static final String INSTRUCTION = "Instruction 1";

    @InjectMocks
    InstructionService instructionService;

    @Mock
    InstructionRepository instructionRepository;

    private static Stream<Arguments> createInstructionExceptionSource() {
        return Stream.of(
            Arguments.of("Invalid Clinic Id", "fk_instruction_lookup_clinic_id")
        );
    }

    @Test
    void createInstruction() {
        final Instruction instruction = getMockInstruction();
        when(instructionRepository.save(any())).thenReturn(instruction);
        final Instruction instructionResponse = instructionService.createInstruction(instruction);
        Assertions.assertNotNull(instructionResponse);
    }

    @ParameterizedTest
    @MethodSource("createInstructionExceptionSource")
    void createInstructionWhenThrowException(final String message, final String constraintName) {
        final Instruction instruction = getMockInstruction();
        final var cve = new ConstraintViolationException(message, null, constraintName);
        final var div = new DataIntegrityViolationException("Test", cve);
        when(instructionRepository.save(any())).thenThrow(div);
        Assertions.assertThrows(InvalidFieldException.class,
            () -> instructionService.createInstruction(instruction));
    }

    @Test
    void listInstructions() {
        final List<Instruction> instructions = getMockInstructionList();
        final InstructionQueryParam instructionQueryParam = InstructionQueryParam.builder()
            .clinicId(CLINIC_ID)
            .build();
        when(instructionRepository.findAllByClinicId(instructionQueryParam.getClinicId()))
            .thenReturn(instructions);
        instructionService.listInstruction(instructionQueryParam);
        final List<Instruction> instructionListResponse =
            instructionService.listInstruction(instructionQueryParam);
        Assertions.assertEquals(1, instructionListResponse.size());
    }

    @Test
    void deleteInstruction() {
        final Instruction instruction = getMockInstruction();
        when(instructionRepository.findByIdAndClinicId(instruction.getId(), CLINIC_ID))
            .thenReturn(Optional.of(instruction));
        instructionService.deleteInstruction(instruction.getId(), CLINIC_ID);
        verify(instructionRepository, times(1))
            .findByIdAndClinicId(instruction.getId(), CLINIC_ID);
    }

    @Test
    void deleteInstructionWhenReturnNull() {
        final Instruction instruction = getMockInstruction();
        when(instructionRepository.findByIdAndClinicId(instruction.getId(), CLINIC_ID))
            .thenReturn(Optional.empty());
        Assertions.assertThrows(DataValidationException.class,
            () -> instructionService.deleteInstruction(INSTRUCTION_ID, CLINIC_ID));
    }

    private Instruction getMockInstruction() {
        final Instruction instruction = new Instruction();
        instruction.setId(INSTRUCTION_ID);
        instruction.setClinic(getMockedClinic());
        instruction.setInstruction(INSTRUCTION);
        return instruction;
    }

    private Clinic getMockedClinic() {
        final Clinic clinic = new Clinic();
        clinic.setId(CLINIC_ID);
        return clinic;
    }

    private List<Instruction> getMockInstructionList() {
        return List.of(getMockInstruction());
    }
}
