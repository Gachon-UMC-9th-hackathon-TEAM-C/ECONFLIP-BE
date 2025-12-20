package com.example.econflip.domain.user.dto;

import com.example.econflip.domain.card.enums.CategoryType;
import lombok.Builder;

@Builder
public record libraryCard(
        // userCard table
        boolean isBookmarked,

        // card table
        Long cardId,
        String term,
        String descript,
        CategoryType category
) {}
