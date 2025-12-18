package com.example.econflip.domain.user.dto;

import com.example.econflip.domain.card.enums.CategoryType;
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

    @Builder
    public record entireLibraryPage(
            List<CategoryType> categories,

            // userCard table, card table
            List<libraryCard> libraryCardList
    ){}

    @Builder
    public record categoryLibraryPage(
            CategoryType category,

            // userCard table, card table
            List<libraryCard> libraryCardList
    ){}
}
