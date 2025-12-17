package com.example.econflip.domain.card.repository;

import com.example.econflip.domain.card.entity.Card;
import com.example.econflip.domain.card.enums.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    // 특정 주제(Category)에 속한 카드들을 고정된 학습 순서를 유지하기 위해 ID 오름차순으로 조회
    List<Card> findByCategoryOrderByIdAsc(CategoryType category);
}
