package com.example.econflip.domain.user.repository;

import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);

    Optional<User> findBySocialTypeAndSocialId(
            SocialType socialType,
            String socialId
    );
}
