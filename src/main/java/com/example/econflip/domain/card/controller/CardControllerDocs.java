package com.example.econflip.domain.card.controller;

import com.example.econflip.domain.card.dto.CardResDTO;
import io.swagger.v3.oas.annotations.Operation;
import com.example.econflip.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface CardControllerDocs {
    // api/card/study/today
    @Operation(
            summary = "오늘의 학습 세트 조회 조회 API",
            description = "랜덤 또는 선택한 tag(주제)로 생성한 하루 분량의 학습 카드 데이터를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<CardResDTO.TodayStudySet> getTodayStudySet(Long userId, Integer daily_study, List<String> selectedCategories);


    // api/card/study/{studySetId}/quiz
    @Operation(
            summary = "퀴즈 답안 제출 API",
            description = "퀴즈 답안을 저장하고 정답 여부를 판단합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    void submitQuizAnswer(Long studySetId);


    // api/card/study/{studySetId}/complete
    @Operation(
            summary = "학습 완료 처리 API",
            description = "학습 진행도를 업데이트하고, 오답을 복습 테이블(review)에 저장합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    CardResDTO.StudyComplete completeStudy(Long studySetId);
}
