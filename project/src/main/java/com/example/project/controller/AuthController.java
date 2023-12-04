package com.example.project.controller;

import com.example.project.configuration.filter.JwtAuthenticationFilter;
import com.example.project.exception.TokenRefreshException;
import com.example.project.model.RefreshToken;
import com.example.project.model.User;
import com.example.project.payload.request.TokenRefreshRequest;
import com.example.project.payload.response.JwtResponse;
import com.example.project.payload.response.MessageResponse;
import com.example.project.payload.response.TokenRefreshResponse;
import com.example.project.service.JwtService;
import com.example.project.service.RefreshTokenService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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

    @Autowired
    private MessageSource messageSource;

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
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        messageSource.getMessage("message.refresh.token.not.in.database",
                                null,
                                LocaleContextHolder.getLocale())));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, Principal principal) {
        String token = JwtAuthenticationFilter.getJwtFromRequest(request);
        if (!jwtService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(
                    messageSource.getMessage("invalid.jwt.token",
                    null,
                    LocaleContextHolder.getLocale())));
        }
        String email = principal.getName();
        User currentUser = userService.findByEmail(email).get();
        refreshTokenService.deleteByUserId(currentUser.getId());
        return ResponseEntity.ok(new MessageResponse(messageSource.getMessage(
                "message.logout.success",
                null,
                LocaleContextHolder.getLocale())));
    }


    @GetMapping("/admin/infor")
    public String admin() {
        return "admin page";
    }

    @GetMapping("/dac/infor")
    public String dac() {
        return "dac page";
    }

    @GetMapping("/advertiser/infor")
    public String advertiser() {
        return "advertiser page";
    }

}
