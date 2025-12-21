package com.example.econflip.global.config.security.oauth.handler;

import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.auth.service.AuthService;
import com.example.econflip.global.config.security.oauth.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;

    @Value("${app.frontend.url:https://localhost:5173}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        //  OAuth 인증된 사용자 꺼내기
        CustomOAuth2User oAuth2User =
                (CustomOAuth2User) authentication.getPrincipal();

        User user = oAuth2User.getUser();

        // 서비스 로그인 (JWT 발급 + 쿠키 세팅)
        authService.login(user, response);

        // OAuth2로 잠깐 인증했던 흔적을 지우고 JWT 인증 체계
        SecurityContextHolder.clearContext();

        // 프론트엔드로 리다이렉트
        String redirectUrl = frontendUrl + "/auth/callback";
        response.sendRedirect(redirectUrl);
    }
}
