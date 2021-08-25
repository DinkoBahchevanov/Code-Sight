package com.codesight.codesight_api.infrastructure.controllerAdvices;

import com.codesight.codesight_api.infrastructure.exceptions.ApiError;
import com.codesight.codesight_api.infrastructure.exceptions.challenges.ChallengeAlreadyExistsException;
import com.codesight.codesight_api.infrastructure.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ChallengeControllerAdvice {

    @ExceptionHandler(ChallengeAlreadyExistsException.class)
    public @ResponseBody ResponseEntity<Object> handleChallengeAlreadyExistsException(
            ChallengeAlreadyExistsException ex) {
        return new ResponseEntity<>(new ApiError(LocalDateTime.now()+"", ex.getMessage(),
                "api/v1/challenges", BAD_REQUEST.value()+""), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChallengeNotFoundException.class)
    public @ResponseBody ResponseEntity<Object>  handleChallengeNotFoundException(
            ChallengeNotFoundException ex) {
        return new ResponseEntity<>(new ApiError(LocalDateTime.now()+"", ex.getMessage(),
                "api/v1/challenges", BAD_REQUEST.value()+""), HttpStatus.NOT_FOUND);
    }
}

