package com.codesight.codesight_api.infrastructure.controllerAdvices;
import com.codesight.codesight_api.infrastructure.exceptions.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class DtoValidationControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(LocalDateTime.now()+"", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURI(), BAD_REQUEST.value()+"");

        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }
}

