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
            String term,
            String descript,
            String example
    ){}

    @Builder
    public record QuizQuestion  (
            Long quizId,
            String question,
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
