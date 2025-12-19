package com.example.econflip.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserReqDTO {
    public record BadgeSelectReqDTO(
            @NotNull
            @Size(max = 4)
            List<Long> badgeIds
    ){}
}
