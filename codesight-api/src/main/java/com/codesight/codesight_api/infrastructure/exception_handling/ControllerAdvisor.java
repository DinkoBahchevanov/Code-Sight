package com.codesight.codesight_api.infrastructure.exception_handling;

import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges.InvalidPointsRangeException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IncorrectJsonMergePatchProcessingException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.shared.IdCannotBeChangedException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users.UserAlreadyExistsException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users.UserAuthenticationException;
import com.codesight.codesight_api.infrastructure.exception_handling.exceptions.users.UserNotFoundException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), NOT_FOUND.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectJsonMergePatchProcessingException.class)
    public ResponseEntity<Object> handleJsonMergeProcessingException(IncorrectJsonMergePatchProcessingException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(IdCannotBeChangedException.class)
    public ResponseEntity<Object> handleIdCannotBeChangedException(IdCannotBeChangedException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
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
        return new ResponseEntity<>(new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(),
                errors, httpServletRequest.getRequestURI()), BAD_REQUEST);
    }
    @ExceptionHandler(ChallengeNotFoundException.class)
    public ResponseEntity<Object>  handleChallengeNotFoundException(
            ChallengeNotFoundException ex, HttpServletRequest httpServletRequest) {
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(),
                ex.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(body, NOT_FOUND);
    }

    @ExceptionHandler(InvalidPointsRangeException.class)
    public ResponseEntity<Object> handleInvalidPointsRangeException(InvalidPointsRangeException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), BAD_REQUEST.value(), ex.getMessage(), httpRequest.getRequestURI());
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), FORBIDDEN.value(), "You don't have access here", httpRequest.getRequestURI());
        return new ResponseEntity<>(body, FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, HttpServletRequest httpRequest){
        ApiError body = new ApiError(OffsetDateTime.now(), UNAUTHORIZED.value(), "Wrong credentials", httpRequest.getRequestURI());
        return new ResponseEntity<>(body, UNAUTHORIZED);
    }
}
