package com.codesight.codesight_api.web.dtos.user;

import com.codesight.codesight_api.domain.user.entity.ApplicationUserRole;
import com.codesight.codesight_api.infrastructure.custom_annotations.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserPostDto {

    @NotNull(message = "Name field is required")
    @Size(min = 2, max = 100, message = "Name length should be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Email field is required")
    @Email(message = "Email address must be well-formed")
    private String email;

    @ValidPassword
    @NotNull(message = "Password field is required")
    private String password;

    @NotNull(message = "Role field is required")
    private ApplicationUserRole role;

    public UserPostDto(String name, String email, String password, ApplicationUserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UserPostDto() { }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ApplicationUserRole getRole() {
        return role;
    }

    public void setRole(ApplicationUserRole role) {
        this.role = role;
    }
}
