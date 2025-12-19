package com.example.econflip.domain.card.controller;

import com.example.econflip.domain.card.dto.CardReqDTO;
import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.exception.code.CardSuccessCode;
import com.example.econflip.domain.card.service.CardService;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class CardController implements CardControllerDocs{
    private final CardService cardService;

    // 오늘의 학습 세트 조회
    @Override
    @PostMapping("/today")
    public ApiResponse<CardResDTO.TodayStudySet> startTodayStudySet(
            @RequestParam Long userId,
            @RequestParam Integer daily_study,
            @RequestParam(required = false) List<String> selectedCategories
    ) {
        return ApiResponse.onSuccess(
                CardSuccessCode.CARD_OK,
                cardService.startTodayStudySet(userId, daily_study, selectedCategories));
    }

    // 카드 학습 완료 처리
    @Override
    @PostMapping("/card/{cardId}/confirm")
    public void confirmCard(
            @RequestParam Long userId,
            @PathVariable Long cardId
    ) {
        cardService.confirmCard(userId, cardId);
    }

    // 퀴즈 답안 저장
    @Override
    @PostMapping("/quiz/{cardId}/answer")
    public ApiResponse<CardResDTO.QuizAnswer> submitQuizAnswer(
            @PathVariable Long userId,
            @PathVariable Long cardId,
            @RequestBody CardReqDTO.QuizAnswer request
    ) {
        return ApiResponse.onSuccess(
                CardSuccessCode.QUIZ_OK,
                cardService.submitQuizAnswer(user.getId(), cardId, request));
    }

    // 학습 완료 처리
    @Override
    @PostMapping("/study/{studySetId}/complete")
    public CardResDTO.StudyComplete completeStudy(
            @PathVariable Long studySetId
    ) {
        return null;
    }
}
