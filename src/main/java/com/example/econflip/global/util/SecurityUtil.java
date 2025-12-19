package com.example.econflip.global.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public final class SecurityUtil {

    private SecurityUtil() {
        // util class
    }

    public static Long getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        String principal = authentication.getName();

        if ("anonymousUser".equals(principal)) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        try {
            return Long.valueOf(principal);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("유효하지 않은 사용자 ID 형식입니다: " + principal, e);
        }
    }
}
