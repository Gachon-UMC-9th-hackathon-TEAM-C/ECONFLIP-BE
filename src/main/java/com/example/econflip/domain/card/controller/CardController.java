package com.example.econflip.domain.card.controller;

import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.service.CardService;
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
    public CardResDTO.TodayStudySet getTodayStudySet(
            @RequestParam(required = false) Long tagId
    ) {
        return null;
    }

    // 퀴즈 문제 조회
    @Override
    @GetMapping("/study/{studySetId}/quiz")
    public List<CardResDTO.QuizQuestion> getQuiz(
            @PathVariable Long studySetId
    ) {
        return null;
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
