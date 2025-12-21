package com.example.econflip.domain.user.service;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.Badge;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.entity.mapping.UserBadge;
import com.example.econflip.domain.user.entity.mapping.UserCard;
import com.example.econflip.domain.user.enums.QuizResult;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.BadgeRepository;
import com.example.econflip.domain.user.repository.UserBadgeRepository;
import com.example.econflip.domain.user.repository.UserCardRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserCardRepository userCardRepository;
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

    // 복습 10개 이상 진행
    @Transactional
    public UserResDTO.BadgeInfo checkTenReviewBadge(User user) {
        Long badgeId = 6L;

        int count = userCardRepository.countByUser_IdAndQuizResult(user.getId(), QuizResult.REVIEW_COMPLETE);
        if (count < 10) return null;

        if (userBadgeRepository.existsByUser_IdAndBadge_Id(user.getId(), badgeId)) return null;

        return giveBadge(user, badgeId);
    }

    // 뱃지 정보
    private UserResDTO.BadgeInfo getBadgeInfo(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new UserException(UserErrorCode.BADGE_NOT_FOUND));

        return UserResDTO.BadgeInfo.builder()
                .badgeId(badge.getId())
                .title(badge.getTitle())
                .build();
    }

    // 학습 완료 시 획득 가능한 모든 뱃지를 체크하고 반환
    @Transactional
    public List<UserResDTO.BadgeInfo> checkAndGiveStudyCompleteBadges(User user,
                                                                      List<UserCard> todayUserCards,
                                                                      int todayCorrectCount,
                                                                      int todayTotalCount) {
        List<UserResDTO.BadgeInfo> newBadges = new ArrayList<>();

        // 뱃지 1: 첫 카드 학습시에 획득
        UserResDTO.BadgeInfo badge1 = checkFirstCardBadge(user, todayUserCards);
        if (badge1 != null) newBadges.add(badge1);

        // 뱃지 2: 일일 학습 완주 누적 3회일시 획득
        UserResDTO.BadgeInfo badge2 = checkThreeDayStreakBadge(user);
        if (badge2 != null) newBadges.add(badge2);

        // 뱃지 3: 최근 5일 연속 정답률 80% 이상(최소 30문항)일시 획득
        UserResDTO.BadgeInfo badge3 = checkFiveDayAccuracyBadge(user);
        if (badge3 != null) newBadges.add(badge3);

        // 뱃지 4: 하루 퀴즈 10개 이상 풀이 + 정답률 100% 획득
        UserResDTO.BadgeInfo badge4 = checkPerfectDayBadge(user, todayCorrectCount, todayTotalCount);
        if (badge4 != null) newBadges.add(badge4);

        // 뱃지 5: 4개 카테고리에서 각각 5개 이상 용어 학습 시점에 획득
        UserResDTO.BadgeInfo badge5 = checkFourCategoryBadge(user);
        if (badge5 != null) newBadges.add(badge5);

        // 뱃지 6: 난이도4 콘텐츠 누적 20개 이상 학습 + 누적 정답률 70% 이상일시 획득
        UserResDTO.BadgeInfo badge6 = checkDifficulty4Badge(user);
        if (badge6 != null) newBadges.add(badge6);

        // 뱃지 7: 7일 이상 미접속 후 복귀 당일 일일 학습 완주 + 퀴즈 10문항 달성 시 획득
        UserResDTO.BadgeInfo badge7 = checkReturnBadge(user, todayTotalCount);
        if (badge7 != null) newBadges.add(badge7);

        //뱃지: 복습 10문제 이상 진행시 획득
        UserResDTO.BadgeInfo badge8 = checkTenReviewBadge(user);
        if(badge8 != null) newBadges.add(badge8);

        return newBadges;
    }

    // 첫 카드 학습시에 획득
    @Transactional
    public UserResDTO.BadgeInfo checkFirstCardBadge(User user, List<UserCard> todayUserCards) {
        Long badgeId = 1L;
        Long userId = user.getId();

        // 이미 획득한 뱃지인지 확인
        if (userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            return null;
        }

        // 오늘 시작 시점 계산
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();

        // 오늘 이전에 학습 완료된 카드가 있는지 확인
        long completedBeforeToday = userCardRepository.countCompletedCardsBeforeToday(userId, todayStart);
        if (completedBeforeToday > 0) {
            return null; // 이미 이전에 학습 완료한 카드가 있으면 첫 학습 완료가 아님
        }

        // 오늘 생성된 userCard가 하나도 없으면 획득 불가
        if (todayUserCards.isEmpty()) {
            return null;
        }

        // 오늘 생성된 모든 userCard가 학습 완료 상태인지 확인
        boolean allCompleted = todayUserCards.stream()
                .allMatch(uc -> uc.isConfirmed() && uc.getQuizResult() != QuizResult.UNSEEN);
        if (!allCompleted) {
            return null;
        }

        return giveBadge(user, badgeId);
    }

    // 일일 학습 완주 누적 3회일시 획득
    @Transactional
    public UserResDTO.BadgeInfo checkThreeDayStreakBadge(User user) {
        Long badgeId = 3L;
        Long userId = user.getId();

        if (userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            return null;
        }
        if(user.getStreak() != 3) {
            return null;
        }

        return giveBadge(user, badgeId);
    }

    // 최근 5일 연속 정답률 80% 이상(최소 30문항)일시 획득
    @Transactional
    public UserResDTO.BadgeInfo checkFiveDayAccuracyBadge(User user) {
        Long badgeId = 4L; // 뱃지 ID는 실제 DB의 badge 테이블 ID로 설정 필요
        Long userId = user.getId();

        if (userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            return null;
        }
        LocalDate today = LocalDate.now();

        // 최근 5일 연속으로 조건을 만족하는지 확인
        for (int i = 0; i < 5; i++) {
            LocalDate checkDate = today.minusDays(i);
            LocalDateTime start = checkDate.atStartOfDay();
            LocalDateTime end = start.plusDays(1);

            // 해당 날짜의 UserCard 조회 (퀴즈를 푼 것만)
            List<UserCard> dayCards = userCardRepository
                    .findByUserIdAndCreatedAtBetweenWithQuizResult(userId, start, end);

            // 최소 30문항 체크
            if (dayCards.size() < 30) {
                return null;
            }

            // 정답률 80% 이상 체크
            long correctCount = dayCards.stream()
                    .filter(uc -> uc.getQuizResult() == QuizResult.CORRECT).count();
            double accuracy = (double) correctCount / dayCards.size() * 100;
            if (accuracy < 80.0) {
                return null;
            }
        }

        return giveBadge(user, badgeId);
    }

    // 하루 퀴즈 10개 이상 풀이 + 정답률 100% 획득
    @Transactional
    public UserResDTO.BadgeInfo checkPerfectDayBadge(User user, int todayCorrectCount, int todayTotalCount) {
        Long badgeId = 5L;
        Long userId = user.getId();

        // 일일 학습량이 10개 미만이면 획득 불가능
        if (todayTotalCount < 10) {
            return null;
        }

        if (userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            return null;
        }

        // 퀴즈 10개 이상 풀이 + 정답률 100%
        if (todayTotalCount >= 10 && todayCorrectCount == todayTotalCount) {
            return giveBadge(user, badgeId);
        }
        return null;
    }

    // 4개 카테고리에서 각각 5개 이상 용어 학습 시점에 획득
    @Transactional
    public UserResDTO.BadgeInfo checkFourCategoryBadge(User user) {
        Long badgeId = 7L;
        Long userId = user.getId();

        if (userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            return null;
        }

        // 4개 카테고리 모두 확인
        for (CategoryType category : CategoryType.values()) {
            long count = userCardRepository.countConfirmedCardsByUserIdAndCategory(userId, category);
            if (count < 5) {
                return null;
            }
        }
        // 모든 카테고리에서 5개 이상 학습 완료 시 뱃지 부여
        return giveBadge(user, badgeId);
    }

    // 난이도4 콘텐츠 누적 20개 이상 학습 + 누적 정답률 70% 이상일시 획득
    @Transactional
    public UserResDTO.BadgeInfo checkDifficulty4Badge(User user) {
        Long badgeId = 8L;
        Long userId = user.getId();

        if (userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            return null;
        }

        // 난이도4 카드들의 UserCard 조회 (퀴즈를 푼 것만)
        List<UserCard> difficulty4Cards = userCardRepository.findByUserIdAndCardDifficulty4(userId);

        // 20개 이상 학습했는지 확인
        if (difficulty4Cards.size() < 20) {
            return null;
        }

        // 정답률 70% 이상 확인
        long correctCount = difficulty4Cards.stream()
                .filter(uc -> uc.getQuizResult() == QuizResult.CORRECT)
                .count();
        double accuracy = (double) correctCount / difficulty4Cards.size() * 100;
        if (accuracy < 70.0) {
            return null;
        }

        return giveBadge(user, badgeId);
    }

    // 7일 이상 미접속 후 복귀 당일 일일 학습 완주 + 퀴즈 10문항 달성 시 획득
    @Transactional
    public UserResDTO.BadgeInfo checkReturnBadge(User user, int todayTotalCount) {
        Long badgeId = 9L;
        Long userId = user.getId();

        // 일일 학습량이 10개 미만이면 획득 불가능
        if (todayTotalCount < 10) {
            return null;
        }

        if (userBadgeRepository.existsByUser_IdAndBadge_Id(userId, badgeId)) {
            return null;
        }
        LocalDate today = LocalDate.now();
        LocalDate lastStudyDate = user.getLastStudyDate();

        // lastStudyDate가 null이면 획득 불가
        if (lastStudyDate == null) {
            return null;
        }

        // 7일 이상 미접속인지 확인
        // lastStudyDate로부터 오늘까지의 일수를 계산 (마지막 학습일 당일은 포함하지 않음)
        long daysSinceLastStudy = java.time.temporal.ChronoUnit.DAYS.between(lastStudyDate, today);
        if (daysSinceLastStudy < 7) {
            return null;
        }

        return giveBadge(user, badgeId);
    }

    // 뱃지를 사용자에게 부여하는 공통 메서드
    private UserResDTO.BadgeInfo giveBadge(User user, Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new UserException(UserErrorCode.BADGE_NOT_FOUND));

        // 이미 획득한 뱃지인지 다시 한 번 확인 (중복 방지)
        if (userBadgeRepository.existsByUser_IdAndBadge_Id(user.getId(), badgeId)) {
            return null;
        }

        // UserBadge 생성 및 저장
        UserBadge userBadge = UserBadge.builder()
                .user(user)
                .badge(badge)
                .build();

        userBadgeRepository.save(userBadge);

        return getBadgeInfo(badgeId);
    }
}
