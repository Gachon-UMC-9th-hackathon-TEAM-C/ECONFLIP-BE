package com.example.econflip.domain.card.controller;

import com.example.econflip.domain.card.dto.CardReqDTO;
import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.exception.code.CardSuccessCode;
import com.example.econflip.domain.card.service.CardService;
import com.example.econflip.domain.user.dto.UserCardReqDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController implements CardControllerDocs{
    private final CardService cardService;

    // 오늘의 학습 세트 조회
    @Override
    @PostMapping("/study-sessions/today")
    public ApiResponse<CardResDTO.TodayStudySet> startTodayStudySet(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam(required = false) List<String> selectedCategories
    ) {
        return ApiResponse.onSuccess(
                CardSuccessCode.CARD_OK,
                cardService.startTodayStudySet(user.getId(), selectedCategories));
    }

    // 카드 학습 완료 처리
    @Override
    @PostMapping("/{cardId}/confirm")
    public ApiResponse<Void> confirmCard(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable Long cardId,
            @RequestBody UserCardReqDTO.DontKnowReqDTO request
    ) {
        cardService.confirmCard(user.getId(), cardId, request);
        return ApiResponse.onSuccess(CardSuccessCode.CARD_OK, null);
    }

    // 퀴즈 답안 저장
    @Override
    @PostMapping("/{cardId}/quiz/answer")
    public ApiResponse<CardResDTO.QuizAnswer> submitQuizAnswer(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable Long cardId,
            @RequestBody CardReqDTO.QuizAnswer request
    ) {
        return ApiResponse.onSuccess(
                CardSuccessCode.QUIZ_OK,
                cardService.submitQuizAnswer(user.getId(), cardId, request));
    }

    // 학습 완료 처리
    @Override
    @PostMapping("/study-sessions/today/complete")
    public ApiResponse<CardResDTO.StudyComplete> completeStudy(
            @AuthenticationPrincipal(expression = "user") User user
    ) {
        return ApiResponse.onSuccess(
                CardSuccessCode.QUIZ_OK,
                cardService.completeTodayStudy(user));
    }
}