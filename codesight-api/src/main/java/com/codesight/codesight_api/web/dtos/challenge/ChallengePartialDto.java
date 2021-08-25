package com.codesight.codesight_api.web.dtos.challenge;

import com.codesight.codesight_api.domain.challenge.entity.Difficulty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChallengePartialDto {private String name;
    private String description;
    private Difficulty difficulty;
    private Integer points;

    public ChallengePartialDto() {

    }

    public ChallengePartialDto(String name, String description, Difficulty difficulty, int points) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.points = points;
    }

    @NotBlank(message = "Challenge name cannot be null")
    @Size(min = 2, max = 100, message = "Challenge name should be between 2 and 100 characters")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank(message = "Challenge description cannot be null")
    @Size(min = 1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull(message = "Difficulty cannot be null")
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Min(value = 1, message = "Points should be at least with value '1'")
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
