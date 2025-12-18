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

    public UserCardResDTO.entireLibraryPage getEntireLibraryPage(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.Not_Found);
        }
        // 나중에 파라미터 단에서 예외처리

        List<CategoryType> categories = List.of(CategoryType.values());

        List<libraryCard> libraryCardList =
                userCardRepository.findLibraryCardByUserId(userId);

        UserCardResDTO.entireLibraryPage page
                = toEntireLibraryPageDTO(categories, libraryCardList);

        return page;
    }

    public UserCardResDTO.categoryLibraryPage getCategoryLibraryPage(Long userId, CategoryType category){
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.Not_Found);
        }
        // 나중에 파라미터 단에서 예외처리

        List<libraryCard> list =
                userCardRepository.findCategoryLibraryCardByUserId(userId, category);

        UserCardResDTO.categoryLibraryPage page
                = toCategoryLibraryPage(category, list);

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

    private UserCardResDTO.entireLibraryPage toEntireLibraryPageDTO(
            List<CategoryType> categories,
            List<libraryCard> libraryCardList
    ){
        return UserCardResDTO.entireLibraryPage.builder()
                .categories(categories)
                .libraryCardList(libraryCardList)
                .build();
    }

    private UserCardResDTO.categoryLibraryPage toCategoryLibraryPage(
            CategoryType category,
            List<libraryCard> list
    ){
        return UserCardResDTO.categoryLibraryPage.builder()
                .category(category)
                .libraryCardList(list)
                .build();
    }
}
