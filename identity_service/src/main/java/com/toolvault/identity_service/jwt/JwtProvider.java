package com.toolvault.identity_service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
  private final Key key; private final int accessExpMin; private final String issuer;
  public JwtProvider(@Value("${jwt.secret}") String secret,
                     @Value("${jwt.access-exp-min}") int accessExpMin,
                     @Value("${jwt.issuer}") String issuer) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessExpMin = accessExpMin; this.issuer = issuer;
  }
  public String issueAccessToken(Long userId, String email, String role) {
    Instant now = Instant.now();
    return Jwts.builder()
      .setSubject(String.valueOf(userId)).setIssuer(issuer)
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plusSeconds(accessExpMin*60L)))
      .claim("email", email).claim("role", role)
      .signWith(key, SignatureAlgorithm.HS256).compact();
  }
    public String issueRefreshToken() {
        return java.util.UUID.randomUUID().toString();
    }
  public Jws<Claims> parse(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }
}
