package com.test.api.healthcare.configurations.services;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_CLINIC_NOT_FOUND;
import static com.test.api.healthcare.configurations.models.entities.Instruction.FK_INSTRUCTION_LOOKUP_CLINIC_ID;

import com.test.api.healthcare.common.constants.ErrorMessage;
import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.models.InstructionQueryParam;
import com.test.api.healthcare.configurations.models.entities.Instruction;
import com.test.api.healthcare.configurations.repositories.InstructionRepository;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstructionService {

    private final InstructionRepository instructionRepository;

    public Instruction createInstruction(final Instruction instruction) {
        try {
            return instructionRepository.save(instruction);
        } catch (final DataIntegrityViolationException ex) {
            handleCreateInstructionCreationFailure(ex, instruction);
            throw ex;
        }
    }

    private void handleCreateInstructionCreationFailure(final DataIntegrityViolationException ex,
                                                        final Instruction instruction) {
        if (ex.getCause() instanceof ConstraintViolationException constraintViolationException) {
            final String constraintName = constraintViolationException.getConstraintName();
            if (constraintName.equals(FK_INSTRUCTION_LOOKUP_CLINIC_ID)) {
                throw new InvalidFieldException("instruction", "clinicId",
                    String.format(ERR_MSG_CLINIC_NOT_FOUND, instruction.getClinic().getId()));
            }
        }
    }

    public List<Instruction> listInstruction(final InstructionQueryParam instructionQueryParam) {
        return instructionRepository.findAllByClinicId(instructionQueryParam.getClinicId());
    }

    public void deleteInstruction(final Long instructionId, final Long clinicId) {
        final Instruction instruction =
            instructionRepository.findByIdAndClinicId(instructionId, clinicId)
                .orElseThrow(() -> new DataValidationException(
                    String.format(ErrorMessage.ERR_MSG_INSTRUCTION_NOT_FOUND, instructionId, clinicId)));
        instructionRepository.delete(instruction);
    }

    public boolean existsById(final Long instructionId) {
        return instructionRepository.existsById(instructionId);
    }
}
