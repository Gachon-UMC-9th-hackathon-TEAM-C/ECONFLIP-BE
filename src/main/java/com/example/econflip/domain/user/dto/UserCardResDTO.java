package com.example.econflip.domain.user.dto;

import com.example.econflip.domain.card.enums.TagType;
import lombok.Builder;

import java.util.List;

public class UserCardResDTO {

    @Builder
    public record reviewPage(
            // userCard table
            int totalReviewCount,

            List<reviewCard> reviewCardList,

            // no domain
            int estimatedDurationMinutes
    ){}

    @Builder
    public record reviewCard(
            // card table
            String term,
            TagType tagType  // 나중에 CategoryType 으로 바꾸기
    ){}
}
