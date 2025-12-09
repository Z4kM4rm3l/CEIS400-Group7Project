package com.toolvault.identity_service.auth.dto;

public record TokenPairResponse(
        String accessToken,
        String refreshToken
) { }
