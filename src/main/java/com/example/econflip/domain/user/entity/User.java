package com.example.econflip.domain.user.entity;

import com.example.econflip.domain.user.enums.SocialType;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 id값 생성
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SocialType socialType;

    @Column(name = "social_key", nullable = false)
    private String socialId;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "level", nullable = false)
    @Builder.Default
    private Integer level = 1;

    @Column(name = "xp", nullable = false)
    @Builder.Default
    private Integer xp = 0;

    @Column(name = "streak", nullable = false)
    @Builder.Default
    private Integer streak = 0;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean status = true;

    @Column(name = "is_learned", nullable = false)
    @Builder.Default
    private Boolean isLearned = false;

    @Column(name = "daily_study", nullable = false)
    private Integer dailyStudy;

    public void updateDailyStudy(Integer count) {
        if (count != 5 && count != 10) {
            throw new UserException(UserErrorCode.INVALID_DAILY_STUDY);
        }
        this.dailyStudy = count;
    }
}