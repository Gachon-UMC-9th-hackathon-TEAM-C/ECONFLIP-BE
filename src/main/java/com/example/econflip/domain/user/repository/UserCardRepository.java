package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.reviewCard;
import com.example.econflip.domain.user.entity.Badge;
import com.example.econflip.domain.user.entity.User;
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
}
