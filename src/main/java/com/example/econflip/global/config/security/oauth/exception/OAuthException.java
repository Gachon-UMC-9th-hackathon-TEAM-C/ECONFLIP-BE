package com.example.econflip.global.config.security.oauth.exception;

import com.example.econflip.global.apiPayload.code.BaseCode;
import com.example.econflip.global.apiPayload.exception.GeneralException;

public class OAuthException extends GeneralException {
  public OAuthException(BaseCode code) {
    super(code);
  }
}
