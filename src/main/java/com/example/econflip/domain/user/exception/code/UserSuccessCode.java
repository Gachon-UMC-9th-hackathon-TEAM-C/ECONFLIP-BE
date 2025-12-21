package com.example.econflip.domain.user.exception.code;

import com.example.econflip.global.apiPayload.code.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements BaseCode {
    OK(HttpStatus.OK,
            "USER200_1",
            "해당 사용자를 찾았습니다."),
    MYPAGE_OK(HttpStatus.OK,
            "USER200_2",
            "마이페이지 조회에 성공했습니다."),
    HOMEPAGE_OK(HttpStatus.OK,
            "USER200_3",
            "홈 페이지 조회에 성공했습니다."),
    DAILY_STUDY_UPDATED(HttpStatus.OK,
            "USER200_4",
            "오늘의 학습 설정이 변경되었습니다."),
    BADGES_OK(HttpStatus.OK,
            "USER200_5",
            "유저가 획득한 배지 조회에 성공했습니다."),
    MYPAGE_BADGES_OK(HttpStatus.OK,
            "USER200_6",
            "마이페이지 배지 구성이 변경되었습니다."),
    REVIEW_PAGE_OK(HttpStatus.OK,
            "USER200_7",
                    "리뷰페이지 조회에 성공했습니다."),
    ENTIRE_LIBRARY_OK(HttpStatus.OK,
            "USER200_8",
            "전체 라이브러리 페이지 조회에 성공했습니다."),
    CATEGORY_LIBRARY_OK(HttpStatus.OK,
            "USER200_9",
            "카테고리 라이브러리 페이지 조회에 성공했습니다."),
    BOOKMARK_OK(HttpStatus.OK,
            "USER200_10",
            "북마크 업데이트에 성공했습니다.")
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
