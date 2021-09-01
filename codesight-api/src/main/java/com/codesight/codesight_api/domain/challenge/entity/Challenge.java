package com.codesight.codesight_api.domain.challenge.entity;

import com.codesight.codesight_api.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Challenge extends BaseEntity {

    @NotNull(message = "Challenge name can't be null")
    @Size(min = 2, max = 100, message = "Challenge name length should be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Description can't be null")
    private String description;

    @NotNull(message = "Difficulty can't be null")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Min(value = 1, message = "Points should not be less than 1")
    @Max(value = 300, message = "Points should not be greater than 300")
    private int points;

    public Challenge(String name, String description, Difficulty difficulty, int points) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.points = points;
    }

    public Challenge(int id, String name, String description, Difficulty difficulty, int points) {
        super.setId(id);
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.points = points;
    }
    public Challenge() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getPoints() {
        return points;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
