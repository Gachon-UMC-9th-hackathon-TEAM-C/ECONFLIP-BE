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
// 마이페이지: 유저 칭호 + xp + 레벨 + 다음 레벨까지 남은 xp + 레벨별 진행률 + 연속학습 + 학습한 용어 개수 + 획득한 배지() + 북마크한 용어
//return ReviewResDTO.MyReviewDTO.builder()
//                .userName(review.getMember().getName())
//                .storeName(review.getStore().getName())
//                .star(review.getStar())
//                .body(review.getContent())
//                .createdAt(LocalDate.from(review.getCreatedAt()))
//                .build();