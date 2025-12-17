package com.example.econflip.domain.user.dto;

import lombok.Builder;

import java.util.List;

public class UserResDTO {
    @Builder
    public record UserMyPage(
            String title,
            String imageUrl,
            Integer level,
            Integer xp,
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