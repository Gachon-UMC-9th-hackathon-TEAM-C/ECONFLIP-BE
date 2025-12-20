package com.example.econflip.global.auth.exception;

import com.example.econflip.global.apiPayload.code.BaseCode;
import com.example.econflip.global.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseCode code) {
        super(code);
    }
}
