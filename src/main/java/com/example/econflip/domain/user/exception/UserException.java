package com.example.econflip.domain.user.exception;

import com.example.econflip.global.apiPayload.code.BaseCode;
import com.example.econflip.global.apiPayload.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(BaseCode code){
        super(code);
    }
}
