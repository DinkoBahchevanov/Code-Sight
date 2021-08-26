package com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges;

public class ChallengeAlreadyExistsException extends RuntimeException {

    public ChallengeAlreadyExistsException(String message) {
        super(message);
    }
}
