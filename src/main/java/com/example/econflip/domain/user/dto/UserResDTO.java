package com.example.econflip.domain.user.dto;

import lombok.Builder;

import java.util.List;

public class UserResDTO {
    @Builder
    public record UserMyPage(
            String title,
            String imageUrl,
            Integer currentLevel,
            Integer currentXp,
            Integer requiredXpForNextLevel,
            Integer remainingXpToNextLevel,
            Integer streak,
            Integer totalLearnedCard,
            List<BadgeStatus> badges,
            Integer totalBookmarkedCard
    ){}

    @Builder
    public record UserHomePage(
            Integer streak,
            Integer level,
            Boolean isLearned,
            Integer dailyGoalCount,
            Integer studyCompletedCardCount,
            Integer quizCompletedCardCount,
            Integer reviewRequiredCardCount,
            List<String> recommendedCategory
    ){}

    @Builder
    public record UserSetting(
            Integer dailyStudy
    ){}

    @Builder
    public record BadgeStatus(
            String title,
            boolean earned
    ) {}
}