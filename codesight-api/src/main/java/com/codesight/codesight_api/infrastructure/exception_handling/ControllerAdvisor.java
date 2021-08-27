package com.codesight.codesight_api.infrastructure.exception_handling;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IncorrectJsonMergePatchProcessingException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ChallengeNotFoundException.class)
    public ResponseEntity<Object>  handleChallengeNotFoundException(
            ChallengeNotFoundException ex, HttpServletRequest httpServletRequest) {
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(),
                ex.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(body, NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ArrayList<String> errors = new ArrayList<>();

        for ( FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for ( ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), errors,
                ((ServletWebRequest) request).getRequest().getRequestURI() );

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object>  handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest httpServletRequest) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraintViolation -> errors.add(constraintViolation.getMessage()));
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), errors, httpServletRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectJsonMergePatchProcessingException.class)
    public ResponseEntity<Object> handleJsonMergeProcessingException(IncorrectJsonMergePatchProcessingException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }
}

