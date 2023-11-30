package com.example.project.payload.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

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
