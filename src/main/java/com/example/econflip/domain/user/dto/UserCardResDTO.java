package com.example.econflip.domain.user.dto;

import lombok.Builder;

import java.util.List;

public class UserCardResDTO {

    @Builder
    public record reviewPage(
            // userCard table
            int totalReviewCount,

            // card table
            List<reviewCard> reviewCardList,

            // no domain
            int estimatedDurationMinutes
    ){}
}
