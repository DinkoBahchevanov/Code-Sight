package com.codesight.codesight_api.web.dtos.challenge;

import com.codesight.codesight_api.domain.challenge.entity.Difficulty;
import javax.validation.constraints.*;

public class ChallengePostDto {

    @NotBlank(message = "Challenge name field is required")
    @Size(min = 2, max = 100, message = "Challenge name should be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Challenge description field is required")
    private String description;

    @NotNull(message = "Difficulty field is required")
    private Difficulty difficulty;

    @Min(value = 1, message = "Points should not be less than 1")
    @Max(value = 300, message = "Points should not be greater than 300")
    private Integer points;

    public ChallengePostDto() {

    }

    public ChallengePostDto(String name, String description, Difficulty difficulty, int points) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.points = points;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
