package com.example.econflip.global.auth.exception;

import com.example.econflip.global.apiPayload.code.BaseCode;
import com.example.econflip.global.apiPayload.exception.GeneralException;

public class AuthExcetpion extends GeneralException {
    public AuthExcetpion(BaseCode code) {
        super(code);
    }
}
