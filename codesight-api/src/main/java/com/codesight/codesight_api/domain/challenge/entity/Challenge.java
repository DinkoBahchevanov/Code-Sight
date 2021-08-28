package com.codesight.codesight_api.domain.challenge.entity;

import com.codesight.codesight_api.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table
public class Challenge extends BaseEntity {

    private String name;
    private String description;
    private Difficulty difficulty;
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

    @Column
    @NotNull(message = "Challenge name is required")
    @Size(min = 2, max = 100, message = "Challenge name should be between 2 and 100 characters")
    public String getName() {
        return name;
    }

    @Column
    @NotNull(message = "Description is required")
    public String getDescription() {
        return description;
    }

    @Column
    @NotNull(message = "Difficulty is required")
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Column
    @NotNull(message = "Points is required")
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
