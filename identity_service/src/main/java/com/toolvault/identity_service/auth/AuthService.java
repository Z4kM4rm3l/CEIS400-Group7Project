package com.toolvault.identity_service.auth;

import com.toolvault.identity_service.auth.dto.LoginRequest;
import com.toolvault.identity_service.auth.dto.RegisterRequest;
import com.toolvault.identity_service.auth.dto.TokenPairResponse;

public interface AuthService {
    TokenPairResponse register(RegisterRequest request);
    TokenPairResponse login(LoginRequest request);
    TokenPairResponse refresh(String refreshToken);
}
