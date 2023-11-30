package com.example.project.service;

import com.example.project.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken generateRefreshToken(Long userId);

    int deleteByUserId(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);
}
