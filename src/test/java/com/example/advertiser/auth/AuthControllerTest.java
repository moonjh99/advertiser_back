package com.example.advertiser.auth;


import com.example.advertiser.auth.dto.LoginRequest;
import com.example.advertiser.auth.dto.SignupRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void signup_success() throws Exception{
        SignupRequest req = new SignupRequest();
        req.setEmail("user@test.com");
        req.setPassword("password");
        req.setName("문문문");

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
        .andExpect(jsonPath("$.data.userId").isNumber())
        .andExpect(jsonPath("$.data.email").value("user@test.com"));
    }

    @Test
    void login_success() throws Exception {
        // 먼저 회원가입
        SignupRequest signup = new SignupRequest();
        signup.setEmail("login@test.com");
        signup.setPassword("password123");
        signup.setName("로그인유저");

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup))
        ).andExpect(status().isOk());

        // 로그인
        LoginRequest login = new LoginRequest();
        login.setEmail("login@test.com");
        login.setPassword("password123");

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    void login_fail_wrong_password() throws Exception {
        SignupRequest signup = new SignupRequest();
        signup.setEmail("fail@test.com");
        signup.setPassword("password123");
        signup.setName("실패유저");

        mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup))
        ).andExpect(status().isOk());

        LoginRequest login = new LoginRequest();
        login.setEmail("fail@test.com");
        login.setPassword("wrongpassword");

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

}
