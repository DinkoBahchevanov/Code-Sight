package com.codesight.codesight_api.domain.user.entity;

import com.codesight.codesight_api.infrastructure.custom_annotations.ValidPassword;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "Name can't be null")
    @Column(nullable = false)
    @Size(min = 2, max = 100, message = "Name length should be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Email can't be null")
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @NotNull(message = "Password can't be null")
    @Column(nullable = false)
    @ValidPassword
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role can't be null")
    private ApplicationUserRole role;

    public User(Integer id, String name, String email, String password, ApplicationUserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }
}
