package com.example.project.service;

import com.example.project.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends GeneralService<User>, UserDetailsService {
    Optional<User> findByEmail(String email);
}
