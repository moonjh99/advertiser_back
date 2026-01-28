package com.example.advertiser.auth;

import com.example.advertiser.auth.jwt.JwtProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class JwtAuthFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void access_secure_api_with_valid_token() throws Exception{
        String token = jwtProvider.issueAccessToken(100L);

        mockMvc.perform(
                get("/api/secure").header("Authorization","Bearer "+token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data",is(100)));
    }

    @Test
    void access_secure_api_without_token_fail() throws Exception {
        mockMvc.perform(get("/api/secure"))
                .andExpect(status().isUnauthorized());
    }
}
