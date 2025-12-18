package com.example.econflip.domain.user.service;

import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.dto.reviewCard;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.UserCardRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;

    public UserCardResDTO.reviewPage getReviewPage(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.Not_Found);
        }
        // 나중에 파라미터 단에서 예외처리

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
