package com.example.econflip.domain.user.dto;

public class UserReqDTO {
    public record UpdateSetting(
            Integer dailyStudy
    ){}
}
