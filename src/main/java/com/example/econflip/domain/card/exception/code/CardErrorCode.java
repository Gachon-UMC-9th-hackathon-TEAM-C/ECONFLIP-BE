package com.example.econflip.domain.card.exception.code;

import com.example.econflip.global.apiPayload.code.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardErrorCode implements BaseCode {
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND,
            "CARD404_1",
            "카드를 찾을 수 없습니다."),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND,
            "QUIZ404_1",
            "퀴즈를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND,
            "CATEGORY404_1",
            "존재하지 않는 카테고리입니다."),
    QUIZ_ALREADY_ANSWERED(HttpStatus.BAD_REQUEST,
            "QUIZ400_2",
            "이미 완료한 퀴즈입니다."),
    STUDY_CARD_NOT_FINISHED(HttpStatus.BAD_REQUEST,
            "STUDYCARD400_1",
            "완료하지 않은 카드 학습이 존재합니다."),
    STUDY_QUIZ_NOT_FINISHED(HttpStatus.BAD_REQUEST,
            "STUDYQUIZ400_1",
            "완료하지 않은 퀴즈가 존재합니다.")
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
