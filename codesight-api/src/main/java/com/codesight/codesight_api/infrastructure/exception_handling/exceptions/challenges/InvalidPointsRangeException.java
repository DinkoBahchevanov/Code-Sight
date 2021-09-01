package com.codesight.codesight_api.infrastructure.exception_handling.exceptions.challenges;

import com.codesight.codesight_api.domain.challenge.entity.Difficulty;

public class InvalidPointsRangeException extends RuntimeException {

    public InvalidPointsRangeException(int points, Difficulty difficulty) {
        super(String.format("Invalid points value '%d' for Difficulty level '%s'", points, difficulty));
    }
}
