package com.example.econflip.domain.card.controller;

import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.exception.code.CardSuccessCode;
import com.example.econflip.domain.card.service.CardService;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController implements CardControllerDocs{
    private final CardService cardService;

    // 오늘의 학습 세트 조회
    @Override
    @GetMapping("/study/today")
    public ApiResponse<CardResDTO.TodayStudySet> getTodayStudySet(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestParam Integer daily_study,
            @RequestParam(required = false) List<String> selectedCategories
    ) {
        return ApiResponse.onSuccess(
                CardSuccessCode.OK,
                cardService.getTodayStudySet(user.getId(), daily_study, selectedCategories));
    }

    // 퀴즈 답안 저장
    @Override
    @PostMapping("/study/{studySetId}/quiz")
    public void submitQuizAnswer(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable Long studySetId
    ) {
    }

    // 학습 완료 처리
    @Override
    @PostMapping("/study/{studySetId}/complete")
    public CardResDTO.StudyComplete completeStudy(
            @AuthenticationPrincipal(expression = "user") User user,
            @PathVariable Long studySetId
    ) {
        return null;
    }
}
