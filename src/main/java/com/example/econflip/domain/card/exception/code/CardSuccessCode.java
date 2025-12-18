package com.example.econflip.domain.card.exception.code;

import com.example.econflip.global.apiPayload.code.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardSuccessCode implements BaseCode {
    OK(HttpStatus.OK,
            "CODE200_1",
            "카드 조회에 성공했습니다."),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
