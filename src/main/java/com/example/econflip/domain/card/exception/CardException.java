package com.example.econflip.domain.card.exception;

import com.example.econflip.global.apiPayload.code.BaseCode;
import com.example.econflip.global.apiPayload.exception.GeneralException;

public class CardException extends GeneralException {
    public CardException(BaseCode code){
        super(code);
    }
}
