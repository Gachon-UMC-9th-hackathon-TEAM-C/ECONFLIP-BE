package com.example.econflip.domain.user.dto;

import com.example.econflip.domain.card.enums.CategoryType;
import lombok.Builder;

import java.util.List;

public class UserCardReqDTO {
    public record DontKnowReqDTO(
            boolean dontKnow
    ){}
}
