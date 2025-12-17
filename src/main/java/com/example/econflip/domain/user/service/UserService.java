package com.example.econflip.domain.user.service;

import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.entity.mapping.UserBadge;
import com.example.econflip.domain.user.entity.mapping.UserTitle;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.UserBadgeRepository;
import com.example.econflip.domain.user.repository.UserCardRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import com.example.econflip.domain.user.repository.UserTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserCardRepository userCardRepository;

    public UserResDTO.UserMyPage getMypage(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.Not_Found));

        String titleName = getLatestTitleName(userId);

        List<String> badgeTitles = getRecentBadgeTitles(userId);

        int totalLearnedCard = getTotalLearnedCardCount(userId);

        int totalBookmarkedCard = getTotalBookmarkedCardCount(userId);

        return UserResDTO.UserMyPage.builder()
                .title(titleName)
                .level(user.getLevel())
                .imageUrl(user.getImageUrl())
                .xp(user.getXp())
                .streak(user.getStreak())
                .totalLearnedCard(totalLearnedCard)
                .totalBookmarkedCard(totalBookmarkedCard)
                .badges(badgeTitles)
                .build();
    }

    // 칭호 최신 1개 가져오기
    private String getLatestTitleName(Long userId) {
        UserTitle userTitle = userTitleRepository
                .findTopByUser_IdOrderByCreatedAtDesc(userId)
                .orElse(null);

        if (userTitle == null || userTitle.getTitle() == null) {
            return "경제 학습자";
        }
        return userTitle.getTitle().getTitle();
    }

    // 배지 최신 4개 가져오기
    private List<String> getRecentBadgeTitles(Long userId) {
        List<UserBadge> recentBadges = userBadgeRepository
                .findTop4ByUser_IdOrderByCreatedAtDesc(userId);

        return recentBadges.stream()
                .map(userBadge -> userBadge.getBadge().getTitle())
                .toList();
    }

    // 학습 카드 총 개수
    private int getTotalLearnedCardCount(Long userId) {
        return userCardRepository.countByUser_Id(userId);
    }

    // 북마크 카드 총 개수
    private int getTotalBookmarkedCardCount(Long userId) {
        return userCardRepository.countByUser_IdAndIsBookmarkedTrue(userId);
    }
}
