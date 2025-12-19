package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.user.entity.Badge;
import com.example.econflip.domain.user.entity.mapping.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findByUser_IdOrderByUpdatedAtDesc(Long userId);

    @Query("""
                select b
                from Badge b
                where b.id not in (
                    select ub.badge.id
                    from UserBadge ub
                    where ub.user.id = :userId
                )
                order by b.id asc
            """)
    List<Badge> findNotEarnedBadges(Long userId, Pageable pageable);
}
