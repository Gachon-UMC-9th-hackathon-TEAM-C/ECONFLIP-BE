package com.example.econflip.domain.user.dto;

import lombok.Builder;

import java.util.List;

public class UserResDTO {
    @Builder
    public record UserMyPage(
            String name,
            String title,
            String imageUrl,
            Integer currentLevel,
            Integer currentXp,
            Integer requiredXpForNextLevel,
            Integer remainingXpToNextLevel,
            Integer streak,
            Integer totalLearnedCard,
            List<String> badges,
            Integer totalBookmarkedCard
    ){}

    @Builder
    public record UserSetting(
            Integer dailyStudy
    ){}
}
