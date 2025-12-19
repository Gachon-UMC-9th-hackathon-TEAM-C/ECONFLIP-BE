package com.example.econflip.domain.card.repository;

import com.example.econflip.domain.card.entity.Card;
import com.example.econflip.domain.card.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCard(Card card);

    @Query("""
        SELECT q
        FROM Quiz q
        WHERE q.card.id = :cardId
          AND q.is_answer = true
    """)
    Optional<Quiz> findCorrectQuizByCardId(@Param("cardId") Long cardId);

    Optional<Quiz> findById(Long quizId);
}
