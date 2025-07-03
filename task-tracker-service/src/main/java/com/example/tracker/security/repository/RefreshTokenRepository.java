package com.example.tracker.security.repository;

import com.example.tracker.domain.model.User;
import com.example.tracker.security.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Query("""
            SELECT token
            FROM RefreshToken token
            WHERE token.user = :user AND token.isRevoked = false
            ORDER BY token.expiryDate DESC
            """)
    Optional<RefreshToken> findValidTokenByUser(@Param("user") User user);
}
