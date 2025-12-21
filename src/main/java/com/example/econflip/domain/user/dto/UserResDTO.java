package com.example.econflip.domain.user.dto;

import com.example.econflip.domain.card.enums.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class UserResDTO {
    @Builder
    public record UserMyPage(
            @Schema(description = "현재 선택된 유저 칭호", example = "경제 학습자")
            String title,
            @Schema(
                description = "유저 프로필 이미지 URL",
                example = "https://cdn.econflip.com/profile.png"
            )
            String imageUrl,

            @Schema(description = "현재 레벨", example = "3")
            Integer currentLevel,

            @Schema(description = "현재 경험치", example = "120")
            Integer currentXp,

            @Schema(description = "다음 레벨까지 필요한 총 경험치", example = "250")
            Integer requiredXpForNextLevel,

            @Schema(description = "다음 레벨까지 남은 경험치", example = "130")
            Integer remainingXpToNextLevel,

            @Schema(description = "연속 학습 일수(streak)", example = "5")
            Integer streak,

            @Schema(description = "총 학습 완료 카드 수", example = "22")
            Integer totalLearnedCard,

            @Schema(description = "4개 배지 목록")
            List<BadgeStatus> badges,

            @Schema(description = "북마크한 카드 개수", example = "7")
            Integer totalBookmarkedCard
    ) {}

    @Builder
    public record UserHomePage(

            @Schema(description = "연속 학습 일수(streak)", example = "5")
            Integer streak,

            @Schema(description = "현재 유저 레벨", example = "3")
            Integer level,

            @Schema(description = "오늘 학습을 완료했는지 여부", example = "true")
            Boolean isLearned,

            @Schema(description = "오늘 설정된 일일 학습 목표 카드 수", example = "5")
            Integer dailyGoalCount,

            @Schema(description = "오늘 학습 완료한 카드 수", example = "4")
            Integer studyCompletedCardCount,

            @Schema(description = "오늘 퀴즈를 완료한 카드 수", example = "3")
            Integer quizCompletedCardCount,

            @Schema(description = "복습이 필요한 카드 수", example = "2")
            Integer reviewRequiredCardCount,

            @Schema(description = "추천 학습 주제 및 주제별 학습 완료 카드 수")
            List<CategoryCount> recommendedCategory,

            @Schema(description = "이번 학습으로 새로 획득한 배지 정보")
            BadgeInfo earnedBadge
    ) {}

    @Builder
    public record BadgeStatus(
            @Schema(description = "배지 ID", example = "1")
            Long badgeId,
            @Schema(description = "배지 제목", example = "첫 플립")
            String title,
            @Schema(description = "배지 획득 여부", example = "true")
            boolean earned
    ) {}

    @Builder
    public record BadgeInfo(
            @Schema(description = "배지 ID", example = "1")
            Long badgeId,
            @Schema(description = "배지 제목", example = "첫 플립")
            String title
    ){}

    @Getter
    @AllArgsConstructor
    public static class CategoryCount {
        @Schema(description = "카테고리", example = "INVESTMENT")
        private CategoryType category;
        @Schema(description = "학습한 카드 개수", example = "5")
        private Long isLearnedCount;
    }
}
