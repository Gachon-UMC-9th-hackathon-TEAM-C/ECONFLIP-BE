package com.example.econflip.domain.card.dto;

import lombok.Builder;

import java.util.List;

public class CardResDTO {
    @Builder
    public record TodayStudySet (
            Long studySetId,
            List<StudyCard> cards
    ){}

    @Builder
    public record StudyCard (
            Long cardId,
            String category,
            String term,
            String descript,
            String example,
            List<String> relatedTerms // 관련 용어
    ){}

    @Builder
    public record QuizQuestion  (
            Long quizId,
            String question,
            String quiz_type,
            List<QuizChoice> choices
    ){}

    @Builder
    public record QuizChoice  (
            Long answerId,
            String content
    ){}

    @Builder
    public record StudyComplete (
            Integer correctRate,
            Integer xp
    ){}
}
