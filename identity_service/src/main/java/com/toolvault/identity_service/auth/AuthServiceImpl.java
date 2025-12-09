package com.toolvault.identity_service.auth;

import com.toolvault.identity_service.auth.dto.LoginRequest;
import com.toolvault.identity_service.auth.dto.RegisterRequest;
import com.toolvault.identity_service.auth.dto.TokenPairResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public TokenPairResponse register(RegisterRequest request) {
        // TODO: implement user creation, password hashing, token issuing
        return new TokenPairResponse("stub-access-token", "stub-refresh-token");
    }

    @Override
    public TokenPairResponse login(LoginRequest request) {
        // TODO: implement credential verification and token issuing
        return new TokenPairResponse("stub-access-token", "stub-refresh-token");
    }

    @Override
    public TokenPairResponse refresh(String refreshToken) {
        // TODO: verify refresh token and issue new access/refresh tokens
        return new TokenPairResponse("new-access-token", "new-refresh-token");
    }
}
