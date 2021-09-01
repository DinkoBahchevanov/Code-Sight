package com.codesight.codesight_api.web.dtos.user;

import com.codesight.codesight_api.domain.user.entity.ApplicationUserRole;

public class UserGetDto {

    private int id;
    private String name;
    private String email;
    private ApplicationUserRole role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ApplicationUserRole getRole() {
        return role;
    }

    public void setRole(ApplicationUserRole role) {
        this.role = role;
    }
}
