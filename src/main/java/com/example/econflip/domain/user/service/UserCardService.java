package com.example.econflip.domain.user.service;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.dto.UserCardResDTO;
import com.example.econflip.domain.user.dto.libraryCard;
import com.example.econflip.domain.user.dto.reviewCard;
import com.example.econflip.domain.user.entity.mapping.UserCard;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.UserCardRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final BadgeService badgeService;

    public UserCardResDTO.reviewPage getReviewPage(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.NOT_FOUND);
        }
        // 나중에 파라미터 단에서 예외처리

        List<reviewCard> reviewCardList = Optional
                .ofNullable(userCardRepository.findReviewByUserId(userId))
                .orElse(List.of());
        int totalReviewCount = reviewCardList.size();

        UserCardResDTO.reviewPage reviewPage
            = toReviewPageDto(totalReviewCount, reviewCardList, totalReviewCount);
        // estimatedDurationMinutes 계산 기준 : 용어 1개당 퀴즈 1개 & 퀴즈 1개당 1분

        return reviewPage;
    }

    @Transactional
    public void completeReview(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.NOT_FOUND);
        }

        List<UserCard> userCardList = userCardRepository.findByUserIdAndIsConfirmed(userId, true);
        userCardList.forEach(UserCard::updateReviewComplete);
        badgeService.tenReviewBadge(userId);
    }

    public UserCardResDTO.libraryPage getEntireLibraryPage(Long userId){
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        List<CategoryType> categories = List.of(CategoryType.values());

        List<libraryCard> libraryCardList = Optional
                .ofNullable(userCardRepository.findLibraryCardByUserId(userId))
                .orElse(List.of());

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

        List<libraryCard> list = Optional
                .ofNullable(userCardRepository.findCategoryLibraryCardByUserId(userId, category))
                .orElse(List.of());

        UserCardResDTO.libraryPage page
                = toLibraryPageDTO(categories, list);

        return page;
    }

    @Transactional
    public UserCardResDTO.bookmarkClick updateBookmark(Long userId, Long cardId){
        userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));
        if (cardId == null || cardId <= 0){
            throw new UserException(UserErrorCode.CARD_NOT_FOUND);
        }
        int updated = userCardRepository.toggleBookmark(userId, cardId);
        if(updated==0) throw new UserException(UserErrorCode.BOOKMARK_FAILED);

        boolean bookmarked = userCardRepository.findBookmark(userId, cardId)
                .orElseThrow(() -> new UserException(UserErrorCode.BOOKMARK_FAILED));
        // 또는 orElse(false)
        return toBookmarkClick(cardId, bookmarked);
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

    private UserCardResDTO.bookmarkClick toBookmarkClick(
            Long id,
            boolean bookmarked
    ){
        return UserCardResDTO.bookmarkClick.builder()
                .cardId(id)
                .bookmarked(bookmarked)
                .build();
    }
}
