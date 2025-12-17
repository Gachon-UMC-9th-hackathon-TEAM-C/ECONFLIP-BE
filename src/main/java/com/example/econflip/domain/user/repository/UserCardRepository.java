package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.user.entity.mapping.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    // 유저가 학습한 전체 카드 수
    int countByUser_Id(Long userId);
    // 유저가 북마크한 카드 수
    int countByUser_IdAndIsBookmarkedTrue(Long userId);
}
