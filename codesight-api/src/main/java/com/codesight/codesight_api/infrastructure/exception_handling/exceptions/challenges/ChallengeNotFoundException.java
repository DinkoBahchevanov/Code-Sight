package com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges;

public class ChallengeNotFoundException extends RuntimeException {

    public ChallengeNotFoundException(String message) {
        super(message);
    }
}
