package com.example.advertiser.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;
    private final long accessMinutes;

    public JwtProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-minutes}") long accessMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessMinutes = accessMinutes;
    }

    public String issueAccessToken(Long userId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessMinutes * 60);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public JwtClaims parse(String token) {
        var payload = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long userId = Long.valueOf(payload.getSubject());
        String email = payload.get("email", String.class);
        return new JwtClaims(userId, email);
    }

    public record JwtClaims(Long userId, String email) {}

}
