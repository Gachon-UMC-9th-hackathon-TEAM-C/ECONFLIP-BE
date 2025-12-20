package com.example.econflip.domain.user.entity;

import com.example.econflip.domain.user.enums.Role;
import com.example.econflip.domain.user.enums.SocialType;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static com.example.econflip.domain.user.enums.Role.USER;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_social",
                        columnNames = {"social_type", "social_id"}
                )
        }
)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 id값 생성
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false, length = 30)
    private SocialType socialType;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, length = 50)
    private String name;

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
    private Boolean active = true;

    @Column(name = "is_learned", nullable = false)
    @Builder.Default
    private Boolean isLearned = false;

    @Column(name = "daily_study", nullable = false)
    @Builder.Default
    private Integer dailyStudy = 5;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = USER;

    public static User createSocialUser(
            SocialType socialType,
            String socialId,
            String name,
            String imageUrl
    ) {
        return User.builder()
                .socialType(socialType)
                .socialId(socialId)
                .name(name)
                .imageUrl(imageUrl)
                .role(USER)
                .build();
    }

    public void updateDailyStudy(Integer count) {
        if (count != 5 && count != 10) {
            throw new UserException(UserErrorCode.INVALID_DAILY_STUDY);
        }
        this.dailyStudy = count;
    }

    public void completeTodayStudy(Integer xp, Integer level) {
        this.xp = xp;
        this.level += level;
        this.streak++;
        this.isLearned = true;
    }
}
