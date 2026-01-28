package com.example.advertiser.auth;

import com.example.advertiser.auth.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void issue_and_parse_token(){
        Long userId = 42L;

        String token = jwtProvider.issueAccessToken(userId);
        JwtProvider.JwtClaims  claims = jwtProvider.parse(token);
        assertThat(claims.userId()).isEqualTo(userId);
    }

}
