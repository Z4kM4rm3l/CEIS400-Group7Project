package com.toolvault.reporting_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final String secret;
    public JwtAuthFilter(String secret) { this.secret = secret; }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }
        String token = header.substring(7);
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new IllegalArgumentException("invalid token");
            String signingInput = parts[0] + "." + parts[1];
            byte[] sig = Base64.getUrlDecoder().decode(parts[2]);

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            byte[] expected = mac.doFinal(signingInput.getBytes());
            if (!java.security.MessageDigest.isEqual(sig, expected)) {
                throw new IllegalArgumentException("bad signature");
            }

            var auth = new UsernamePasswordAuthenticationToken(
                    "reporting-service", null, List.of(new SimpleGrantedAuthority("ROLE_SERVICE")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(req, res);
    }
}
