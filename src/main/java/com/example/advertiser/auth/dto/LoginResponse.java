package com.example.advertiser.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private Long userId;
    private String email;
    private String name;
}
