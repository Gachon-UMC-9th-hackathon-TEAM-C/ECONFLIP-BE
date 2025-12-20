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
            String quizType,
            List<QuizChoice> choices,
            String commentary
    ){}

    @Builder
    public record QuizChoice  (
            Long answerId,
            String answer
    ){}

    @Builder
    public record QuizAnswer (
            Boolean isCorrect,
            String commentary
    ){}

    @Builder
    public record StudyComplete (
            Integer correctCount,
            Integer gainedXp,
            List<String> correctTerms,
            List<String> wrongTerms,
            List<UserResDTO.BadgeInfo> newBadges
    ){}
}
