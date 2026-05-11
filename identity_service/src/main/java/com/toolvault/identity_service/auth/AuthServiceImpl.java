package com.toolvault.identity_service.auth;

import com.toolvault.identity_service.auth.dto.LoginRequest;
import com.toolvault.identity_service.auth.dto.RegisterRequest;
import com.toolvault.identity_service.auth.dto.TokenPairResponse;
import com.toolvault.identity_service.jwt.JwtProvider;
import com.toolvault.identity_service.user.RefreshToken;
import com.toolvault.identity_service.user.RefreshTokenRepository;
import com.toolvault.identity_service.user.User;
import com.toolvault.identity_service.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokens;

    @Value("${jwt.refresh-exp-min:4320}")
    private int refreshExpMin;

    @Override
    @Transactional
    public TokenPairResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        User saved = users.save(user);
        return issueTokenPair(saved);
    }

    @Override
    @Transactional
    public TokenPairResponse login(LoginRequest request) {
        User user = users.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return issueTokenPair(user);
    }

    @Override
    @Transactional
    public TokenPairResponse refresh(String refreshToken) {
        RefreshToken stored = refreshTokens.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (stored.isRevoked()) {
            throw new IllegalArgumentException("Refresh token has been revoked");
        }

        if (stored.isExpired()) {
            stored.setRevoked(true);
            refreshTokens.save(stored);
            throw new IllegalArgumentException("Refresh token has expired — please log in again");
        }

        // Revoke old token — one time use
        stored.setRevoked(true);
        refreshTokens.save(stored);

        // Issue fresh pair
        return issueTokenPair(stored.getUser());
    }

    private TokenPairResponse issueTokenPair(User user) {
        // Revoke all existing refresh tokens for this user
        refreshTokens.deleteAllByUserId(user.getId());

        String accessToken = jwtProvider.issueAccessToken(
                user.getId(), user.getEmail(), user.getRole()
        );

        String rawRefresh = jwtProvider.issueRefreshToken();
        Instant expiresAt = Instant.now().plus(refreshExpMin, ChronoUnit.MINUTES);

        RefreshToken refreshToken = new RefreshToken(rawRefresh, user, expiresAt);
        refreshTokens.save(refreshToken);

        return new TokenPairResponse(accessToken, rawRefresh);
    }
}