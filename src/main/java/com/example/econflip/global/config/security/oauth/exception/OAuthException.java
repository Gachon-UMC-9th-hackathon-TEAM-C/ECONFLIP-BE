package com.example.econflip.global.config.security.oauth.exception;

import com.example.econflip.global.apiPayload.code.BaseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthException extends RuntimeException{
    private final BaseCode code;
}
