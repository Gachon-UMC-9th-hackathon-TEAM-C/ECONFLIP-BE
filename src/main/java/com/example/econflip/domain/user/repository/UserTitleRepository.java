package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.user.entity.mapping.UserTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {

    Optional<UserTitle> findTopByUser_IdOrderByCreatedAtDesc(Long userId);
    boolean existsByUser_Id(Long userId);
}

