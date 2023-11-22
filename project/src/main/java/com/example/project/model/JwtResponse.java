package com.example.project.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtResponse {
    private Long id;
    private String token;
    private String type = "Bearer";
    private String email;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String token, Long id, String email, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.roles = roles;
    }


    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}
