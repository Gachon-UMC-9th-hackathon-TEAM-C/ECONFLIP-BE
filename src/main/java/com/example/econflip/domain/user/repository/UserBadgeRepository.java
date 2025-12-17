package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.user.entity.mapping.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findTop4ByUser_IdOrderByCreatedAtDesc(Long userId);
}
