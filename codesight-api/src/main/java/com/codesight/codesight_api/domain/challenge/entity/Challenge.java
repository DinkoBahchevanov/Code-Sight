package com.codesight.codesight_api.domain.challenge.entity;

import com.codesight.codesight_api.domain.BaseEntity;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Challenge extends BaseEntity {

    private String name;
    private String description;
    private Difficulty difficulty;
    private int points;

    @Column(unique = true)
    @NotNull
    @Size(min = 2, max = 100, message = "Challenge name should be between 2 and 100 characters")
    public String getName() {
        return name;
    }

    @Column
    @NotNull
    public String getDescription() {
        return description;
    }

    @Column
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Column
    @NotNull
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
