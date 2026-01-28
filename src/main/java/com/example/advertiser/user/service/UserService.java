package com.example.advertiser.user.service;

import com.example.advertiser.user.entity.User;
import com.example.advertiser.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public User signup(String email, String rawPassword, String name) {
        if (repo.existsByEmail(email)) throw new IllegalArgumentException("email already exists");
        String hash = encoder.encode(rawPassword);
        User user = User.builder().email(email).passwordHash(hash).name(name).build();
        return repo.save(user);
    }

    public User requireByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    public boolean matchesPassword(User user, String rawPassword) {
        return encoder.matches(rawPassword, user.getPasswordHash());
    }
}
