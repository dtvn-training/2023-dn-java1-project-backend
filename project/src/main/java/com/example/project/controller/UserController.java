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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.repository.Repository;
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
@RequestMapping("/api/user")
public class UserController {

}
