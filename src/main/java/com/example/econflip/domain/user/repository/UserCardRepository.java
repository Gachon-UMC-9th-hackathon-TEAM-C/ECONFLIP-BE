package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.dto.reviewCard;
import com.example.econflip.domain.user.entity.mapping.UserCard;
import com.example.econflip.domain.user.enums.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.econflip.domain.user.dto.libraryCard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    @Query("""
    select new com.example.econflip.domain.user.dto.reviewCard(c.term, c.category)
    from UserCard uc
    join uc.card c
    where uc.user.id = :userId
      and (
           uc.isBookmarked = true
        or uc.quizResult = com.example.econflip.domain.user.enums.QuizResult.WRONG
        or uc.dontKnow = true
      )
""")
    List<reviewCard> findReviewByUserId(Long userId);

    List<UserCard> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    Optional<UserCard> findByUserIdAndCardIdAndCreatedAtBetween(Long userId, Long cardId, LocalDateTime start, LocalDateTime end);

    @Query("""
    select new com.example.econflip.domain.user.dto.libraryCard(uc.isBookmarked, c.term, c.descript, c.category)
    from UserCard uc
    join uc.card c
    where uc.user.id = :userId
""")
    List<libraryCard> findLibraryCardByUserId(Long userId);

    @Query("""
    select new com.example.econflip.domain.user.dto.libraryCard(uc.isBookmarked, c.term, c.descript, c.category)
    from UserCard uc
    join uc.card c
    where uc.user.id = :userId
    and c.category = :category
""")
    List<libraryCard> findCategoryLibraryCardByUserId(@Param("userId") Long userId,
                                                      @Param("category") CategoryType category);

    // 유저가 학습한 전체 카드 수
    int countByUser_IdAndQuizResult(Long userId, QuizResult quizResult);

    // 유저가 북마크한 카드 수
    int countByUser_IdAndIsBookmarkedTrue(Long userId);

    int countByUser_IdAndCreatedAtBetweenAndIsConfirmed(Long userId, LocalDateTime startOfToday, LocalDateTime startOfTomorrow, boolean isConfirmed);

    int countByUser_IdAndQuizResultNotAndCreatedAtBetween(Long userId, QuizResult quizResult, LocalDateTime startOfToday, LocalDateTime startOfTomorrow);

    @Query("""
    select count(uc)
    from UserCard uc
    where uc.user.id = :userId
      and(
           uc.quizResult = :wrong
        or uc.isBookmarked = true
        or uc.dontKnow = true)
""")
    int countReviewRequiredCards(@Param("userId") Long userId, @Param("wrong") QuizResult wrong);

    @Query("""
    select new com.example.econflip.domain.user.dto.UserResDTO$CategoryCount(c.category, count(uc))
    from UserCard uc
    join uc.card c
    where uc.user.id = :userId
      and uc.isConfirmed = true
    group by c.category
    """)
    List<UserResDTO.CategoryCount> getRecCategory(@Param("userId") Long userId);

    // 오늘 이전 날짜에 학습 완료된 카드가 있는지 확인
    @Query("""
    select count(uc)
    from UserCard uc
    where uc.user.id = :userId
      and uc.isConfirmed = true
      and uc.quizResult != com.example.econflip.domain.user.enums.QuizResult.UNSEEN
      and uc.createdAt < :todayStart
    """)
    long countCompletedCardsBeforeToday(@Param("userId") Long userId,
                                        @Param("todayStart") LocalDateTime todayStart);

    // 난이도4 카드들의 UserCard 조회 (퀴즈 결과 포함)
    @Query("""
    select uc
    from UserCard uc
    join uc.card c
    where uc.user.id = :userId
      and c.difficulty = 4
      and uc.quizResult != com.example.econflip.domain.user.enums.QuizResult.UNSEEN
    """)
    List<UserCard> findByUserIdAndCardDifficulty4(@Param("userId") Long userId);

    // 특정 날짜 범위의 UserCard 조회 (정답률 계산용)
    @Query("""
    select uc
    from UserCard uc
    where uc.user.id = :userId
      and uc.createdAt >= :start
      and uc.createdAt < :end
      and uc.quizResult != com.example.econflip.domain.user.enums.QuizResult.UNSEEN
    """)
    List<UserCard> findByUserIdAndCreatedAtBetweenWithQuizResult(@Param("userId") Long userId,
                                                                 @Param("start") LocalDateTime start,
                                                                 @Param("end") LocalDateTime end);

    // 카테고리별 학습 완료된 카드 수 조회 (isConfirmed = true)
    @Query("""
    select count(uc)
    from UserCard uc
    join uc.card c
    where uc.user.id = :userId
      and c.category = :category
      and uc.isConfirmed = true
    """)
    long countConfirmedCardsByUserIdAndCategory(@Param("userId") Long userId,
                                                @Param("category") CategoryType category);
}
