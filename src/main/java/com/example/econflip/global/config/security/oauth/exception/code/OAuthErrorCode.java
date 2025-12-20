package com.example.econflip.global.config.security.oauth.exception.code;

import com.example.econflip.global.apiPayload.code.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OAuthErrorCode implements BaseCode {
    OAUTH_RESPONSE_MAPPING_FAILED(HttpStatus.UNAUTHORIZED,
            "OAUTH401_1",
            "OAuth 인증 응답을 사용자 정보로 변환하는 데 실패했습니다."),
    OAUTH_RESPONSE_MISSING_ID(HttpStatus.UNAUTHORIZED,
            "OAUTH401_2",
            "OAuth 인증 응답에 필수 식별자(id)가 포함되어 있지 않습니다."),
    NOT_SUPPORTED_OAUTH(HttpStatus.BAD_REQUEST,
            "OAUTH400_1",
            "지원하지않는 방식의 소셜로그인입니다."),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
