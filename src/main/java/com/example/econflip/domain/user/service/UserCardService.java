package com.example.econflip.domain.user.service;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.dto.libraryCard;
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
            throw new UserException(UserErrorCode.NOT_FOUND);
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

    public UserCardResDTO.libraryPage getEntireLibraryPage(Long userId){
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        List<CategoryType> categories = List.of(CategoryType.values());

        List<libraryCard> libraryCardList =
                userCardRepository.findLibraryCardByUserId(userId);

        UserCardResDTO.libraryPage page
                = toLibraryPageDTO(categories, libraryCardList);

        return page;
    }

    public UserCardResDTO.libraryPage getCategoryLibraryPage(Long userId, CategoryType category){
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        if (category == null) {
            throw new UserException(UserErrorCode.CATEGORY_NOT_FOUND);
        }

        List<CategoryType> categories = List.of(category);

        List<libraryCard> list =
                userCardRepository.findCategoryLibraryCardByUserId(userId, category);

        UserCardResDTO.libraryPage page
                = toLibraryPageDTO(categories, list);

        return page;
    }

    // converter
    private UserCardResDTO.reviewPage toReviewPageDto(int count, List<reviewCard> list, int min) {
        return UserCardResDTO.reviewPage.builder()
                .totalReviewCount(count)
                .reviewCardList(list)
                .estimatedDurationMinutes(min)
                .build();
    }

    private UserCardResDTO.libraryPage toLibraryPageDTO(
            List<CategoryType> categories,
            List<libraryCard> libraryCardList
    ){
        return UserCardResDTO.libraryPage.builder()
                .categories(categories)
                .libraryCardList(libraryCardList)
                .build();
    }
}
