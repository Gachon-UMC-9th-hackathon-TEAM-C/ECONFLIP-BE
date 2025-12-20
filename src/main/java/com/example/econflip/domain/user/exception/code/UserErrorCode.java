package com.example.econflip.domain.user.exception.code;

import com.example.econflip.global.apiPayload.code.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,
            "USER404_1",
            "해당 사용자를 찾을 수 없습니다."),
    INVALID_DAILY_STUDY(HttpStatus.BAD_REQUEST,
            "USER400_1",
            "하루 학습 분량은 5 또는 10만 설정할 수 있습니다."),
    BADGE_SELECT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST,
            "USER400_2",
            "배지 선택은 최대 4까지 설정할 수 있습니다."),
    BADGE_NOT_OWNED(HttpStatus.BAD_REQUEST,
            "USER400_3",
            "획득한 배지만 선택할 수 있습니다."),
    BADGE_NOT_FOUND(HttpStatus.NOT_FOUND,
            "USER404_2",
            "해당 배지를 찾을 수 없습니다."),
    BOOKMARK_FAILED(HttpStatus.BAD_REQUEST,
            "USER400_4",
            "북마크 업데이트에 실패했습니다.")
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
