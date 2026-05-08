package com.toolvault.identity_service.auth;

import com.toolvault.identity_service.auth.dto.LoginRequest;
import com.toolvault.identity_service.auth.dto.RegisterRequest;
import com.toolvault.identity_service.auth.dto.TokenPairResponse;
import com.toolvault.identity_service.jwt.JwtProvider;
import com.toolvault.identity_service.user.User;
import com.toolvault.identity_service.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public TokenPairResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        User saved = users.save(user);
        String token = jwtProvider.issueAccessToken(saved.getId(), saved.getEmail(), saved.getRole());
        return new TokenPairResponse(token, "refresh-todo");
    }

    @Override
    public TokenPairResponse login(LoginRequest request) {
        User user = users.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtProvider.issueAccessToken(user.getId(), user.getEmail(), user.getRole());
        return new TokenPairResponse(token, "refresh-todo");
    }

    @Override
    public TokenPairResponse refresh(String refreshToken) {
        // TODO: implement refresh token persistence and validation
        throw new UnsupportedOperationException("Refresh not yet implemented");
    }

}