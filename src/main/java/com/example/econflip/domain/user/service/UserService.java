package com.example.econflip.domain.user.service;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.Badge;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.entity.mapping.UserBadge;
import com.example.econflip.domain.user.entity.mapping.UserTitle;
import com.example.econflip.domain.user.enums.QuizResult;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.UserBadgeRepository;
import com.example.econflip.domain.user.repository.UserCardRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import com.example.econflip.domain.user.repository.UserTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

        int reqXp = getRequiredXpForNextLevel(user.getLevel());

        int remXp = getRemainingXpToNextLevel(user.getLevel(), user.getXp());

        UserResDTO.UserMyPage myPage = buildUserMyPage(user,
                titleName,
                badgeTitles,
                totalLearnedCard,
                totalBookmarkedCard,
                reqXp,
                remXp
        );

        return myPage;
    }

    public UserResDTO.UserHomePage getHomePage(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.Not_Found));

        int studyCompletedCardCount = getTodayStudyCompletedCardCount(userId);

        int quizCompletedCardCount = getTodayQuizCompletedCardCount(userId);

        int reviewRequiredCardCount = getReviewRequiredCardCount(userId);

        List<String> recCategory = getRandomRecommendedCategories();

        UserResDTO.UserHomePage homePage
                = buildUserHomePage(
                user,
                studyCompletedCardCount,
                quizCompletedCardCount,
                reviewRequiredCardCount,
                recCategory
        );

        return homePage;
    }


    private UserResDTO.UserHomePage buildUserHomePage(
            User user,
            int studyCompletedCardCount,
            int quizCompletedCardCount,
            int reviewRequiredCardCount,
            List<String> recommendedCategory
    ) {
        return UserResDTO.UserHomePage.builder()
                .streak(user.getStreak())
                .level(user.getLevel())
                .isLearned(user.getIsLearned())
                .dailyGoalCount(user.getDailyStudy())
                .studyCompletedCardCount(studyCompletedCardCount)
                .quizCompletedCardCount(quizCompletedCardCount)
                .reviewRequiredCardCount(reviewRequiredCardCount)
                .recommendedCategory(recommendedCategory)
                .build();
    }

    private UserResDTO.UserMyPage buildUserMyPage(
            User user,
            String titleName,
            List<String> badgeTitles,
            int totalLearnedCard,
            int totalBookmarkedCard,
            int reqXp,
            int remXp
    ) {
        return UserResDTO.UserMyPage.builder()
                .title(titleName)
                .currentLevel(user.getLevel())
                .imageUrl(user.getImageUrl())
                .currentXp(user.getXp())
                .remainingXpToNextLevel(remXp)
                .requiredXpForNextLevel(reqXp)
                .streak(user.getStreak())
                .totalLearnedCard(totalLearnedCard)
                .totalBookmarkedCard(totalBookmarkedCard)
                .badges(badgeTitles)
                .build();
    }

    // 복습 필요한 카드 개수
    private int getReviewRequiredCardCount(Long userId) {
        return userCardRepository.countReviewRequiredCards(userId, QuizResult.WRONG);
    }

    // 오늘 학습완료 카드 개수
    private int getTodayStudyCompletedCardCount(Long userId) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfTomorrow = startOfToday.plusDays(1);

        return userCardRepository.countByUser_IdAndCreatedAtBetween(
                        userId,
                        startOfToday,
                        startOfTomorrow
                );
    }

    // 오늘 퀴즈완료 카드 개수
    private int getTodayQuizCompletedCardCount(Long userId) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfTomorrow = startOfToday.plusDays(1);

        return userCardRepository.countByUser_IdAndQuizResultNotAndCreatedAtBetween(
                userId,
                QuizResult.UNSEEN,
                startOfToday,
                startOfTomorrow
        );
    }

    // 추천 주제 랜덤으로 4개 가져오기
    private List<String> getRandomRecommendedCategories(){
        List<CategoryType> categories = new ArrayList<>(Arrays.asList(CategoryType.values()));

        Collections.shuffle(categories);

        return categories.stream()
                .limit(4)
                .map(Enum::name)
                .toList();
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
                .map(UserBadge::getBadge)
                .filter(Objects::nonNull)
                .map(Badge::getTitle)
                .filter(Objects::nonNull)
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

    // 다음 레벨로 가기 위해 필요한 총 경험치 (해당 레벨 구간 필요 xp)
    private int getRequiredXpForNextLevel(int level){
        return 50 * (level + 1);
    }

    // 다음 레벨까지 남은 경험치
    private int getRemainingXpToNextLevel(int level, int curXp){
        int total = 0;
        for(int i = 1; i <= level; i++){
            total += 50 * (i + 1);
        }
        return Math.max(total - curXp, 0);
    }
}
