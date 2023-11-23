package com.example.project.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

@Data
@Getter
@Setter
@Builder
public class JwtResponse {
    private Long id;
    private String token;
    private String type;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
    private String refreshToken;
    private Long expirationTime;
}
