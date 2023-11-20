package com.test.api.healthcare.common.validators;

import static com.test.api.healthcare.common.constants.ErrorMessage.ERR_MSG_FILE_SIZE_EXCEEDED;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Slf4j
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private static final double BYTE = 1;
    private static final double KIB = BYTE * 1000;
    private static final double MIB = KIB * 1000;

    private Long maxSizeInMB;

    @Override
    public void initialize(final FileSize fileSize) {
        this.maxSizeInMB = fileSize.maxSizeInMB();
    }

    @Override
    public boolean isValid(final MultipartFile multipartFile,
                           final ConstraintValidatorContext constraintValidatorContext) {
        final String messageTemplate = String.format(ERR_MSG_FILE_SIZE_EXCEEDED,
            getFileSizeWithUnit(multipartFile.getSize()), this.maxSizeInMB);

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
        if (multipartFile.getSize() <= maxSizeInMB * MIB) {
            return true;
        } else {
            log.error(ERR_MSG_FILE_SIZE_EXCEEDED);
            return false;
        }
    }

    private String getFileSizeWithUnit(final long fileSizeInBytes) {
        if (fileSizeInBytes > MIB) {
            return fileSizeInBytes / MIB + " MB";
        }

        if (fileSizeInBytes > KIB) {
            return (fileSizeInBytes / KIB) + " KB";
        }

        return fileSizeInBytes + " B";
    }
}
