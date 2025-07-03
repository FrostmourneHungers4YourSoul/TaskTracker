package com.example.tracker.security;

import com.example.tracker.exception.handler.ExceptionResponse;
import com.example.tracker.security.model.SecurityUser;
import com.example.tracker.security.service.CustomUserDetailsService;
import com.example.tracker.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        log.info("Authorization Header: {}", header);

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String username;
        try {
           username = jwtService.extractUsername(token);
        } catch (ExpiredJwtException ex) {
            log.error("ExpiredJwtException: {}", ex.getMessage());
            handleExpiredToken(response);
            return;
        }

        log.info("Username found in JWT Token: {}", username);

        if (StringUtils.hasLength(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            log.info("Create authentication instance for {}", username);

            SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, securityUser)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        securityUser,
                        null,
                        securityUser.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }

    public void handleExpiredToken(HttpServletResponse response) throws IOException {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Token expired",
                "Your session has expired. Please log in again."
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = objectMapper.writeValueAsString(exceptionResponse);
        response.getWriter().write(json);
    }
}
