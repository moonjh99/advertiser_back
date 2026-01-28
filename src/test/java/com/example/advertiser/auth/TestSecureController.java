package com.example.advertiser.auth;

import com.example.advertiser.common.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecureController {

    @GetMapping("/api/secure")
    public ApiResponse<Long> secure(Authentication auth){
        return ApiResponse.ok((Long) auth.getPrincipal());
    }

}
