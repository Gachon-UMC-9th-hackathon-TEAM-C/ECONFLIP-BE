package com.example.econflip.global.config.security.auth.repository;

import com.example.econflip.global.config.security.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
