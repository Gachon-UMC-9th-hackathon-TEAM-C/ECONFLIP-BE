package com.example.econflip.domain.user.dto;

import com.example.econflip.domain.card.enums.CategoryType;
import lombok.Builder;

@Builder
public record reviewCard(
        // card table
        String term,
        CategoryType category,
        String descript,
        String tip,
        String example
) { }
