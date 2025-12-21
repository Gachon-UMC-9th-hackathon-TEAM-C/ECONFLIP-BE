package com.example.econflip.domain.card.dto;

import com.example.econflip.domain.user.dto.UserResDTO;
import lombok.Builder;

import java.util.List;

public class CardResDTO {
    @Builder
    public record TodayStudySet (
            List<StudyCard> cards,
            List<QuizQuestion> quizzes
    ){}

    @Builder
    public record StudyCard (
            Long cardId,
            String category,
            String term,
            String descript,
            String example,
            String tip,
            List<String> relatedTerms // 관련 용어
    ){}

    @Builder
    public record QuizQuestion  (
            Long cardId,
            String question,
            List<QuizChoice> choices,
            String commentary
    ){}

    @Builder
    public record QuizChoice  (
            String term
    ){}

    @Builder
    public record QuizAnswer (
            Boolean isCorrect,
            String commentary,
            String correctAnswer
    ){}

    @Builder
    public record StudyComplete (
            Integer correctCount,
            Integer gainedXp,
            Integer totalStudyCount,
            List<String> correctTerms,
            List<String> wrongTerms,
            List<UserResDTO.BadgeInfo> newBadges
    ){}
}
