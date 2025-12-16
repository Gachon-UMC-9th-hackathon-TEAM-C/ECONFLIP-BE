package com.example.econflip.domain.user.dto;

import lombok.Builder;

public class UserResDTO {
    @Builder
    public record UserMyPage(
            String name,
            String imageUrl,
            Integer level,
            Integer xp,
            Long streak
    ){}

    @Builder
    public record UserSetting(
            Integer dailyStudy
    ){}
}
