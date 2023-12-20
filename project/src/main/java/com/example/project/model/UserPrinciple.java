package com.example.project.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserPrinciple implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String email;

    private String password;

    private GrantedAuthority role;

    public UserPrinciple(Long id,
                         String email, String password,
                         GrantedAuthority role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static UserPrinciple build(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

        return new UserPrinciple(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authority
        );
    }

    @Override
    public  Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
