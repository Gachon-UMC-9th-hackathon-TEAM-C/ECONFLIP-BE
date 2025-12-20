package com.example.econflip.domain.card.controller;

import com.example.econflip.domain.card.dto.CardReqDTO;
import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.user.dto.UserCardReqDTO;
import com.example.econflip.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import com.example.econflip.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface CardControllerDocs {
    @Operation(
            summary = "오늘의 학습 세트 시작/복구 API",
            description = "랜덤 또는 선택한 tag(주제)로 생성한 하루 분량의 학습 카드 데이터를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<CardResDTO.TodayStudySet> startTodayStudySet(User user, List<String> selectedCategories);

    @Operation(
            summary = "카드 학습 완료 처리 API",
            description = "사용자가 학습 중인 카드의 상태를 학습 완료로 변경하여, 진행률을 추적합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    void confirmCard(User user, Long cardId, UserCardReqDTO.DontKnowReqDTO request);


    @Operation(
            summary = "퀴즈 답안 제출 API",
            description = "퀴즈 답안을 저장하고 정답 여부를 판단합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<CardResDTO.QuizAnswer> submitQuizAnswer(User user, Long cardId, CardReqDTO.QuizAnswer request);


    @Operation(
            summary = "학습 완료 처리 API",
            description = "학습 진행도를 업데이트하고, 오답을 복습 테이블(review)에 저장합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    ApiResponse<CardResDTO.StudyComplete> completeStudy(User user);
}
