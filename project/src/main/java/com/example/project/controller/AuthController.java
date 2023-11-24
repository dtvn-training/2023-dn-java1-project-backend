package com.example.project.controller;

import com.example.project.exception.TokenRefreshException;
import com.example.project.model.JwtResponse;
import com.example.project.model.RefreshToken;
import com.example.project.model.User;
import com.example.project.payload.request.TokenRefreshRequest;
import com.example.project.payload.response.MessageResponse;
import com.example.project.payload.response.TokenRefreshResponse;
import com.example.project.service.JwtService;
import com.example.project.service.RefreshTokenService;
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

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateAccessToken(authentication);
        Date expirationDate = jwtService.getExpirationDateFromToken(jwt);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByEmail(user.getEmail()).get();

        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(currentUser.getId());
        var jwtResponse = JwtResponse.builder()
                .token(jwt)
                .id(currentUser.getId())
                .type("Bearer")
                .email(userDetails.getUsername())
                .roles(userDetails.getAuthorities())
                .refreshToken(refreshToken.getToken())
                .expirationTime(expirationDate.getTime())
                .build();
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateTokenFromEmail(user.getEmail());
//                    refreshTokenService.deleteByUserId(user.getId());

                    //refreshToken
//                    RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getId());

                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
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
