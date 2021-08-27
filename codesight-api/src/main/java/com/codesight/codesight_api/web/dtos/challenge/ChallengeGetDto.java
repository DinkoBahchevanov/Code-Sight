package com.codesight.codesight_api.web.dtos.challenge;

import com.codesight.codesight_api.domain.challenge.entity.Difficulty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChallengeGetDto {

    private String name;
    private String description;
    private Difficulty difficulty;
    private Integer points;

    public ChallengeGetDto() {
    }

    public ChallengeGetDto(String name, String description, Difficulty difficulty, Integer points) {
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
