package com.example.econflip.global.config.security.auth.service;

import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.config.security.auth.entity.RefreshToken;
import com.example.econflip.global.config.security.auth.repository.RefreshTokenRepository;
import com.example.econflip.global.config.security.jwt.JwtUtil;
import com.example.econflip.global.exception.UnauthorizedException;
import com.example.econflip.global.util.CookieUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    // OAuth 로그인 성공 후 AccessToken 생성

    @Transactional
    public ResponseEntity<?> login(
            User user,
            HttpServletResponse response
    ) {
        Long userId = user.getId();
        String role = user.getRole().name();

        // access token 발급
        String accessToken =
                jwtUtil.createAccessToken(userId, role);

        // refresh token 발급
        String refreshToken =
                jwtUtil.createRefreshToken(userId);

        // refresh token DB 저장 (있으면 갱신)
        refreshTokenRepository.findById(userId)
                .ifPresentOrElse(
                        saved -> saved.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(
                                RefreshToken.of(userId, refreshToken)
                        )
                );

        // 쿠키 설정
        response.addHeader(
                "Set-Cookie",
                CookieUtil.accessToken(accessToken).toString()
        );
        response.addHeader(
                "Set-Cookie",
                CookieUtil.refreshToken(refreshToken).toString()
        );

        return ResponseEntity.ok().build();
    }

    // AccessToken 재발급
    @Transactional
    public ResponseEntity<?> reissue(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // refresh 토큰 쿠키 확인
        String refreshToken = CookieUtil.get(request, "refreshToken");
        if (refreshToken == null) {
            throw new UnauthorizedException("리프레시 토큰 없음");
        }

        // refresh JWT 검증
        Claims claims = jwtUtil.validateToken(refreshToken);
        Long userId = Long.valueOf(claims.getSubject());

        // DB에 저장된 refresh 토큰 확인
        RefreshToken saved = refreshTokenRepository.findById(userId)
                .orElseThrow(() ->
                        new UnauthorizedException("저장된 리프레시 토큰 없음"));

        if (!saved.getToken().equals(refreshToken)) {
            throw new UnauthorizedException("리프레시 토큰 불일치");
        }

        // 새 AccessToken 발급
        String newAccess =
                jwtUtil.createAccessToken(userId, "USER");

        // (권장) Refresh Token 회전
        String newRefresh =
                jwtUtil.createRefreshToken(userId);
        saved.updateToken(newRefresh);

        // 쿠키 재설정
        response.addHeader(
                "Set-Cookie",
                CookieUtil.accessToken(newAccess).toString()
        );
        response.addHeader(
                "Set-Cookie",
                CookieUtil.refreshToken(newRefresh).toString()
        );

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<?> logout(
            Long userId,
            HttpServletResponse response
    ) {
        refreshTokenRepository.deleteById(userId);

        response.addHeader(
                "Set-Cookie",
                CookieUtil.delete("accessToken").toString()
        );
        response.addHeader(
                "Set-Cookie",
                CookieUtil.delete("refreshToken").toString()
        );

        return ResponseEntity.ok().build();
    }
}
