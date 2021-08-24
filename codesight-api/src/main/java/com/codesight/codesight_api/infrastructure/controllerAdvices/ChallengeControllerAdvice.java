package com.codesight.codesight_api.infrastructure.controllerAdvices;

import com.codesight.codesight_api.infrastructure.exceptions.challenges.ChallengeAlreadyExistsException;
import com.codesight.codesight_api.infrastructure.exceptions.challenges.ChallengeNotFoundException;
import com.codesight.codesight_api.infrastructure.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ChallengeControllerAdvice {

    @ExceptionHandler(ChallengeAlreadyExistsException.class)
    public @ResponseBody ResponseEntity<Object> handleChallengeAlreadyExistsException(
            ChallengeAlreadyExistsException ex) {
        return new ResponseEntity<>(ExceptionUtil.getBody(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChallengeNotFoundException.class)
    public @ResponseBody ResponseEntity<Object>  handleChallengeNotFoundException(
            ChallengeNotFoundException ex) {
        return new ResponseEntity<>(ExceptionUtil.getBody(ex,HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

//    @Bean
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex, HttpHeaders headers,
//            HttpStatus status, WebRequest request) {
//
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDate.now());
//        body.put("status", status.value());
//
//        ArrayList<String> errors = (ArrayList<String>) ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
//
//        body.put("errors", errors);
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
}

