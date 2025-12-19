package com.example.econflip.global.config.security.oauth.handler;

import com.example.econflip.global.config.security.auth.service.AuthService;
import com.example.econflip.global.config.security.oauth.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomOAuth2User)) {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }
        CustomOAuth2User oauth2User = (CustomOAuth2User) principal;

        String accessToken = authService.login(oauth2User.getUser());

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);
        response.sendRedirect("/home");
    }
}
