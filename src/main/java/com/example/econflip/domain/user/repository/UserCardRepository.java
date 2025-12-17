package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.entity.mapping.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    @Query("""
    select new com.example.econflip.domain.user.dto.UserCardResDTO.reviewCard(c.term, c.category)
    from UserCard uc
    join uc.card c
    where uc.user.id = :userId
      and (
           uc.isBookmarked = true
        or uc.quizResult = com.example.econflip.domain.user.enums.QuizResult.WRONG
        or uc.dontKnow = true
      )
""")
    List<UserCardResDTO.reviewCard> findReviewByUserId(Long userId);

}
