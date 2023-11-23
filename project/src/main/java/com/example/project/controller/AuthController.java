package com.example.project.controller;

import com.example.project.model.JwtResponse;
import com.example.project.model.User;
import com.example.project.payload.request.TokenRefreshRequest;
import com.example.project.payload.response.TokenRefreshResponse;
import com.example.project.service.JwtService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateTokenLogin(authentication);
        String jwtRefresh = jwtService.generateRefreshToken(authentication);

        Date expirationDate = jwtService.getExpirationDateFromToken(jwt);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByEmail(user.getEmail()).get();
        var jwtResponse = JwtResponse.builder()
                .token(jwt)
                .id(currentUser.getId())
                .type("Bearer")
                .email(userDetails.getUsername())
                .roles(userDetails.getAuthorities())
                .refreshToken(jwtRefresh)
                .expirationTime(expirationDate.getTime())
                .build();
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        System.out.println(refreshToken);
        if (StringUtils.hasText(refreshToken) && jwtService.validateJwtToken(refreshToken)) {
            Authentication authentication = jwtService.getAuthenticationFromToken(refreshToken);
            String newAccessToken = jwtService.generateTokenLogin(authentication);

            var rs = TokenRefreshResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .build();

            return ResponseEntity.ok(rs);
        } else {
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }
    }

    @GetMapping("/admin/infor")
    public String admin() {
        return "admin page";
    }

    @GetMapping("/user/infor")
    public String user() {
        return "user page";
    }

}
