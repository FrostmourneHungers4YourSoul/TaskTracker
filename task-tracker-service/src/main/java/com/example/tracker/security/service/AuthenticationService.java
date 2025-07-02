package com.example.tracker.security.service;

import com.example.tracker.domain.dto.request.AuthRequest;
import com.example.tracker.domain.dto.request.RefreshTokenRequest;
import com.example.tracker.domain.dto.request.UserRequestDto;
import com.example.tracker.domain.dto.response.AuthResponse;
import com.example.tracker.security.model.RefreshToken;
import com.example.tracker.security.model.SecurityUser;
import com.example.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(UserRequestDto request) {
        userService.create(request);
        return authenticate(new AuthRequest(request.username(), request.password()));
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(request.username());
        String accessToken = jwtService.generateAccessToken(securityUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(securityUser.getId());

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken());
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserById(refreshToken.getUser().getId());

        String newAccessToken = jwtService.generateAccessToken(securityUser);
        return new AuthResponse(newAccessToken, refreshToken.getToken());
    }

    public void logout(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.refreshToken());
        if (Objects.nonNull(refreshToken))
            refreshTokenService.revokeToken(refreshToken);
    }
}
