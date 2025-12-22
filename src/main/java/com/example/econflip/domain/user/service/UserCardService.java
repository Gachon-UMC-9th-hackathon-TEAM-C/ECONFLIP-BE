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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;

    public UserCardResDTO.reviewPage getReviewPage(Long userId){
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

        // 복습이 필요한 카드들만 조회 (북마크, 오답, 모르겠어요)
        List<UserCard> reviewRequiredCards = userCardRepository.findReviewRequiredCardsByUserId(userId);
        reviewRequiredCards.forEach(UserCard::updateReviewComplete);
    }

    public UserCardResDTO.libraryPage getEntireLibraryPage(Long userId){
        List<CategoryType> categories = List.of(CategoryType.values());

        List<libraryCard> libraryCardList = Optional
                .ofNullable(userCardRepository.findLibraryCardByUserId(userId))
                .orElse(List.of());

        UserCardResDTO.libraryPage page
                = toLibraryPageDTO(categories, libraryCardList);

        return page;
    }

    public UserCardResDTO.libraryPage getCategoryLibraryPage(Long userId, CategoryType category){
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

    public UserCardResDTO.libraryPage searchUserCardinEntire(Long userId, String query, int limit) {
        String searchWord = normalize(query);
        int safeLimit = Math.min(Math.max(limit, 1), 20);

        // 전체 카테고리
        List<CategoryType> categories = List.of(CategoryType.values());
        List<libraryCard> libraryCardList;

        if (query.isEmpty()) {
            libraryCardList = List.of();
        }
        else{
            libraryCardList = userCardRepository.findWordByPrefix(userId, searchWord, PageRequest.of(0, safeLimit));
        }

        if(libraryCardList.isEmpty()) throw new UserException(UserErrorCode.CARD_NOT_FOUND);

        UserCardResDTO.libraryPage page
                = toLibraryPageDTO(categories, libraryCardList);

        return page;
    }

    private String normalize(String q) {
        if (q == null) return "";
        // 공백 정리 (연속 공백 -> 1개)
        return q.trim().replaceAll("\\s+", " ");
    }

    public UserCardResDTO.libraryPage searchUserCardinCategory(Long userId, CategoryType category, String query, int limit) {
        String searchWord = normalize(query);
        int safeLimit = Math.min(Math.max(limit, 1), 20);

        List<CategoryType> categories = List.of(category);
        List<libraryCard> libraryCardList;

        if (query.isEmpty()) {
            libraryCardList = List.of();
        }
        else{
            libraryCardList = userCardRepository.findWordByPrefixWithCategory(userId, category, searchWord, PageRequest.of(0, safeLimit));
        }

        if(libraryCardList.isEmpty()) throw new UserException(UserErrorCode.CARD_NOT_FOUND);

        UserCardResDTO.libraryPage page
                = toLibraryPageDTO(categories, libraryCardList);

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