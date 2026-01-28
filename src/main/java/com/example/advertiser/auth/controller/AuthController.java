package com.example.advertiser.auth.controller;

import com.example.advertiser.auth.dto.LoginRequest;
import com.example.advertiser.auth.dto.LoginResponse;
import com.example.advertiser.auth.dto.SignupRequest;
import com.example.advertiser.auth.jwt.JwtProvider;
import com.example.advertiser.common.ApiResponse;
import com.example.advertiser.user.entity.User;
import com.example.advertiser.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ApiResponse<LoginResponse> signup(@Valid @RequestBody SignupRequest req) {
        User user = userService.signup(req.getEmail(), req.getPassword(), req.getName());
        String token = jwtProvider.issueAccessToken(user.getId());
        return ApiResponse.ok(new LoginResponse(token, user.getId(), user.getEmail(), user.getName()));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        User user = userService.requireByEmail(req.getEmail());
        if(!userService.matchesPassword(user, req.getPassword())) {
            throw new IllegalArgumentException("invalid credentials");
        }
        String token = jwtProvider.issueAccessToken(user.getId());
        return ApiResponse.ok(new LoginResponse(token, user.getId(), user.getEmail(), user.getName()));
    }

}
