package com.toolvault.identity_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(
        classes = com.toolvault.identity_service.IdentityServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class AuthSmokeTests {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate rest;

    @Test
    void register_login_me_flow() {
        // Implement your flow or keep as placeholder until endpoints are wired:
        // 1) POST /auth/register (optional if seeding via data.sql)
        // 2) POST /auth/login -> accessToken
        // 3) GET /auth/me with Bearer token -> 200 and expected user
        assertTrue(true); // temporary placeholder
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
