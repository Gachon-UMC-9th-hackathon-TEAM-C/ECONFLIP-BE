package com.example.econflip.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

public class CookieUtil {

    public static ResponseCookie accessToken(String token) {
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .build();
    }

    public static ResponseCookie refreshToken(String token) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .build();
    }

    public static ResponseCookie delete(String name) {
        return ResponseCookie.from(name, "")
                .path("/")
                .maxAge(0)
                .build();
    }

    public static String get(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
