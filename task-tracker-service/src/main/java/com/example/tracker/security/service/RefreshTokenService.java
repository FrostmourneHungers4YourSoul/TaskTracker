package com.example.tracker.security.service;

import com.example.tracker.domain.model.User;
import com.example.tracker.exception.ResourceNotFoundException;
import com.example.tracker.exception.TokenException;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.security.model.RefreshToken;
import com.example.tracker.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpirationMills;

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        refreshTokenRepository.findByUser(user).ifPresent(this::revokeToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plus(refreshExpirationMills, ChronoUnit.MILLIS))
                .isRevoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException("Invalid refresh token."));

        if (refreshToken.getIsRevoked()) {
            throw new TokenException("Refresh token revoked.");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            revokeToken(refreshToken);
            throw new TokenException("Refresh token expired.");
        }

        return refreshToken;
    }

    public void revokeToken(RefreshToken token) {
        token.setIsRevoked(true);
        refreshTokenRepository.save(token);
    }
}
