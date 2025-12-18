package com.example.econflip.global.auth.service;

import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.config.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public String login(User user) {
        return jwtUtil.createAccessToken(
                user.getId(),
                user.getRole().name()
        );
    }
}
