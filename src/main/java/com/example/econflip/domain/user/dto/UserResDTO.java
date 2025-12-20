package com.example.econflip.domain.user.dto;

import com.example.econflip.domain.card.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
            List<CategoryCount> recommendedCategory,
            BadgeInfo earnedBadge
    ){}

    @Builder
    public record BadgeStatus(
            Long badgeId,
            String title,
            boolean earned
    ){}

    @Builder
    public record BadgeInfo(
            Long badgeId,
            String title,
            String comment
    ){}

    @Getter
    @AllArgsConstructor
    public static class CategoryCount {
        private CategoryType category;
        private Long isLearnedCount;
    }
}
