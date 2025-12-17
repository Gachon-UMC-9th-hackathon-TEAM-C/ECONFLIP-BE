package com.example.econflip.domain.user.service;

import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.dto.reviewCard;
import com.example.econflip.domain.user.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;

    public UserCardResDTO.reviewPage getReviewPage(Long userId){

        List<reviewCard> reviewCardList =
                userCardRepository.findReviewByUserId(userId);
        int totalReviewCount = reviewCardList.size();

        UserCardResDTO.reviewPage reviewPage
            = toReviewPageDto(totalReviewCount, reviewCardList, totalReviewCount);
        // estimatedDurationMinutes 계산 기준 : 용어 1개당 퀴즈 1개 & 퀴즈 1개당 1분

        return reviewPage;
    }

    // converter
    private UserCardResDTO.reviewPage toReviewPageDto(int totalReviewCount, List<reviewCard> reviewCardList, int estimatedDurationMinutes) {
        return new UserCardResDTO.reviewPage(
                totalReviewCount,
                reviewCardList,
                estimatedDurationMinutes
        );
    }
}
