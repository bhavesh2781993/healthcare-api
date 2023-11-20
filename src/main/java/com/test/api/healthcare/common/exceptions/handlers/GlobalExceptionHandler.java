package com.test.api.healthcare.common.exceptions.handlers;

import static com.test.api.healthcare.common.constants.ApplicationConstant.DATETIME_FORMAT;
import static com.test.api.healthcare.common.constants.ApplicationConstant.DATE_FORMAT;
import static com.test.api.healthcare.common.constants.ErrorMessage.INVALID_DATA;
import static com.test.api.healthcare.common.constants.ErrorMessage.INVALID_SORT_FILTER_PARAM;
import static com.test.api.healthcare.common.constants.ErrorMessage.MALFORMED_JSON_REQUEST;
import static com.test.api.healthcare.common.constants.ErrorMessage.SERVICE_UNAVAILABLE;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import com.test.api.healthcare.common.constants.ErrorType;
import com.test.api.healthcare.common.exceptions.AuthenticationException;
import com.test.api.healthcare.common.exceptions.AuthorizationException;
import com.test.api.healthcare.common.exceptions.DataAlreadyExistsException;
import com.test.api.healthcare.common.exceptions.DataNotFoundException;
import com.test.api.healthcare.common.exceptions.DataValidationException;
import com.test.api.healthcare.common.exceptions.InvalidCredentialsException;
import com.test.api.healthcare.common.exceptions.InvalidFieldException;
import com.test.api.healthcare.common.exceptions.KeycloakConflictException;
import com.test.api.healthcare.common.models.Error;
import com.test.api.healthcare.common.models.responses.ApiErrorResponse;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.github.perplexhub.rsql.UnknownPropertyException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Slf4j
@Order(value = HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @NonNull
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        final Stream<Error.ErrorDetail> fieldErrorStream =
            getStreamOfErrorDetail(ex.getBindingResult().getFieldErrors(), ErrorType.INVALID_FIELD);
        final Stream<Error.ErrorDetail> globalErrorStream =
            getStreamOfErrorDetail(ex.getBindingResult().getGlobalErrors(), ErrorType.INVALID_DATA);

        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Stream.concat(fieldErrorStream, globalErrorStream).toList());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @NonNull
    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    protected ResponseEntity<Object> handleMissingRequestHeaderException(final MissingRequestHeaderException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(ex.getMessage())
            .type(ErrorType.MISSING_HEADER)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @NonNull
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull final HttpMessageNotReadableException ex) {

        ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(MALFORMED_JSON_REQUEST)
            .type(ErrorType.INVALID_DATA)
            .build());

        if (ex.getCause() instanceof MismatchedInputException mismatchedInputException) {
            final int extraStringInMessage = 10;
            final String errMsg = mismatchedInputException.getOriginalMessage();

            apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
                .message(errMsg.substring(0, errMsg.length() - extraStringInMessage))
                .type(ErrorType.INVALID_DATA)
                .build());
        }

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {
            String errorMsg = "Invalid format";
            if (invalidFormatException.getTargetType().equals(LocalDate.class)) {
                errorMsg = "Date format must match with " + DATE_FORMAT;
            } else if (invalidFormatException.getTargetType().equals(LocalDateTime.class)) {
                errorMsg = "Datetime format must match with " + DATETIME_FORMAT;
            }
            final String fieldName =
                invalidFormatException.getPath().stream().map(JsonMappingException.Reference::getFieldName).findAny().orElse(null);
            if (invalidFormatException.getCause() instanceof DateTimeParseException) {
                apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
                    .field(fieldName)
                    .message(errorMsg)
                    .type(ErrorType.INVALID_FIELD)
                    .build());
            }
        }

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @NonNull
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(ex.getMessage())
            .type(ErrorType.MISSING_PARAM)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<ApiErrorResponse> handleConstraintViolationException(
        final ConstraintViolationException constraintViolationException) {

        final List<Error.ErrorDetail> errorDetails = constraintViolationException.getConstraintViolations()
            .stream()
            .flatMap(constraintViolation -> getStreamOfErrorDetail(constraintViolation, ErrorType.INVALID_DATA))
            .toList();

        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(errorDetails);
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataAlreadyExistsException.class})
    protected ResponseEntity<ApiErrorResponse> handleValueAlreadyPresentException(final RuntimeException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(ex.getMessage())
            .type(ErrorType.DATA_ALREADY_EXIST)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {KeycloakConflictException.class})
    protected ResponseEntity<ApiErrorResponse> handleKeycloakConflictException(final RuntimeException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(ex.getMessage())
            .type(ErrorType.DATA_ALREADY_EXIST)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    protected ResponseEntity<Object> handleRecordNotFoundException(final RuntimeException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(ex.getMessage())
            .type(ErrorType.MISSING_DATA)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidFieldException.class})
    protected ResponseEntity<Object> handleInvalidFieldException(final InvalidFieldException ex) {
        final FieldError fieldError = new FieldError(ex.getObject(), ex.getField(), ex.getMessage());
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(getFieldErrorDetail(fieldError, ErrorType.INVALID_DATA));

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataValidationException.class})
    protected ResponseEntity<Object> handleValidationFailedException(final RuntimeException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(ex.getMessage())
            .type(ErrorType.INVALID_DATA)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityException(final RuntimeException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(INVALID_DATA)
            .type(ErrorType.INVALID_DATA)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {InvalidCredentialsException.class})
    protected ResponseEntity<ApiErrorResponse> invalidCredentialsException(final RuntimeException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(ex.getMessage())
            .type(ErrorType.INVALID_CREDENTIAL)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {UnknownPropertyException.class})
    protected ResponseEntity<ApiErrorResponse> handleUnknownPropertyException(final UnknownPropertyException ex) {
        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(String.format(INVALID_SORT_FILTER_PARAM, ex.getName()))
            .type(ErrorType.INVALID_DATA)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatch Exception=" + ex.getMessage());

        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .field(ex.getPropertyName())
            .message("Invalid value " + ex.getValue())
            .type(ErrorType.INVALID_DATA)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AuthorizationException.class, AccessDeniedException.class})
    protected ResponseEntity<ApiErrorResponse> handleAuthorizationException(final RuntimeException ex) {
        log.error("Forbidden : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<ApiErrorResponse> handleAuthenticationException(final RuntimeException ex) {
        log.error("Unauthorized request : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<ApiErrorResponse> handleRuntimeException(final RuntimeException ex) {
        log.error("Runtime Exception=" + ex.getMessage());

        final ApiErrorResponse apiErrorResponse = getApiErrorResponse(Error.ErrorDetail.builder()
            .message(SERVICE_UNAVAILABLE)
            .type(ErrorType.INTERNAL_ERROR)
            .build());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Stream<Error.ErrorDetail> getStreamOfErrorDetail(final ConstraintViolation<?> constraintViolation, final ErrorType errorType) {
        final var path = (PathImpl) constraintViolation.getPropertyPath();

        return Stream.of(Error.ErrorDetail.builder()
            .field(ObjectUtils.isEmpty(path) ? null : path.getLeafNode().toString())
            .type(errorType)
            .message(constraintViolation.getMessage())
            .build());
    }

    private Stream<Error.ErrorDetail> getStreamOfErrorDetail(final List<? extends ObjectError> objectErrors, final ErrorType errorType) {
        return objectErrors.stream()
            .map(objectError -> {
                if (objectError instanceof FieldError fieldError) {
                    return getFieldErrorDetail(fieldError, errorType);
                } else {
                    return getObjectErrorDetail(objectError, errorType);
                }
            });
    }

    private Error.ErrorDetail getFieldErrorDetail(final FieldError fieldError, final ErrorType errorType) {
        return Error.ErrorDetail.builder()
            .field(fieldError.getField())
            .type(errorType)
            .message(fieldError.getDefaultMessage())
            .build();
    }

    private Error.ErrorDetail getObjectErrorDetail(final ObjectError objectError, final ErrorType errorType) {
        return Error.ErrorDetail.builder()
            .type(errorType)
            .message(objectError.getDefaultMessage())
            .build();
    }

    private ApiErrorResponse getApiErrorResponse(final List<Error.ErrorDetail> errorDetails) {
        return ApiErrorResponse.builder()
            .error(Error.builder()
                .details(errorDetails)
                .build())
            .build();
    }

    private ApiErrorResponse getApiErrorResponse(final Error.ErrorDetail errorDetail) {
        return ApiErrorResponse.builder()
            .error(Error.builder()
                .details(Collections.singletonList(errorDetail))
                .build())
            .build();
    }

}
