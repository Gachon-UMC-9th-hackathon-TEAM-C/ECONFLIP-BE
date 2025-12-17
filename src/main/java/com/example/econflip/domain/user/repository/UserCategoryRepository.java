package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.entity.mapping.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

    // 유저가 선택한 주제들에 대해 주제별 학습 진행도(pointer)를 한 번에 조회
    List<UserCategory> findByUserIdAndCategoryIn(Long userId, List<CategoryType> categories);
}
