package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.user.dto.reviewCard;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.entity.mapping.UserCard;
import com.example.econflip.domain.user.enums.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
    UserCard findByUserIdAndCardIdAndCreatedAtBetween(Long userId, Long cardId, LocalDateTime start, LocalDateTime end);

    // 유저가 학습한 전체 카드 수
    int countByUser_Id(Long userId);
           
    // 유저가 북마크한 카드 수
    int countByUser_IdAndIsBookmarkedTrue(Long userId);
    
    int countByUser_IdAndCreatedAtBetween(Long userId, LocalDateTime startOfToday, LocalDateTime startOfTomorrow);

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

    Long user(User user);
}
