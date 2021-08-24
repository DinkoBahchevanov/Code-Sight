package com.codesight.codesight_api.domain;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {

    private int id;

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
