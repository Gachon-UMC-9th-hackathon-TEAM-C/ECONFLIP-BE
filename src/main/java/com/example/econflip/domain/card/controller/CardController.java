package com.example.econflip.domain.card.controller;

import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.exception.code.CardSuccessCode;
import com.example.econflip.domain.card.service.CardService;
import com.example.econflip.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
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
            @RequestParam Long userId,
            @RequestParam Integer daily_study,
            @RequestParam(required = false) List<String> selectedCategories
    ) {
        return ApiResponse.onSuccess(
                CardSuccessCode.OK,
                cardService.getTodayStudySet(userId, daily_study, selectedCategories));
    }

    // 퀴즈 답안 저장
    @Override
    @PostMapping("/study/{studySetId}/quiz")
    public void submitQuizAnswer(
            @PathVariable Long studySetId
    ) {
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
