package com.example.econflip.domain.user.service;

import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.Badge;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.entity.mapping.UserBadge;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.BadgeRepository;
import com.example.econflip.domain.user.repository.UserBadgeRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    // 연속 7일 이상 출석 배지 획득
    @Transactional
    public UserResDTO.BadgeInfo giveStreakBadge(Long userId, int streak) {
        if(streak < 7) return null;

        Long badgeId = 2L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new UserException(UserErrorCode.BADGE_NOT_FOUND));

        boolean exists = userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId);
        if (exists) return null;

        UserBadge userBadge = UserBadge.builder()
                .user(user)
                .badge(badge)
                .build();

        userBadgeRepository.save(userBadge);

        return getBadgeInfo(badgeId);
    }

    private UserResDTO.BadgeInfo getBadgeInfo(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new UserException(UserErrorCode.BADGE_NOT_FOUND));

        return UserResDTO.BadgeInfo.builder()
                .title(badge.getTitle())
                .comment(badge.getComment())
                .build();
    }
}
