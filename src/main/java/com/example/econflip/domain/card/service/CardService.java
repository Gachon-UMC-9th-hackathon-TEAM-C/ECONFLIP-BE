package com.example.econflip.domain.card.service;

import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.entity.Card;
import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.card.exception.CardException;
import com.example.econflip.domain.card.exception.code.CardErrorCode;
import com.example.econflip.domain.card.repository.CardRepository;
import com.example.econflip.domain.user.entity.mapping.UserCategory;
import com.example.econflip.domain.user.repository.UserCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserCategoryRepository userCategoryRepository;

    // 오늘의 학습 카드 조회 API
    @Transactional
    public CardResDTO.TodayStudySet getTodayStudySet(Long userId, Integer dailyStudy, List<String> selectedCategories)
    {
        // 학습에 사용할 Category 목록
        List<CategoryType> categories = new ArrayList<>();

        // 주제를 선택하지 않은 경우 → 전체 주제
        if (selectedCategories == null || selectedCategories.isEmpty()) {
            categories.addAll(Arrays.asList(CategoryType.values()));
        }
        // 주제를 선택한 경우
        else {
            categories = selectedCategories.stream()
                    .map(CategoryType::valueOf)
                    .toList();
        }

        // 학습분량, 주제개수를 고려해 주제별 카드 개수 분배
        int categoryCount = categories.size();
        int baseCount = dailyStudy / categoryCount;
        int remain = dailyStudy % categoryCount; // 균등 분배 후 남는 카드 수
        int[] allocateCounts = new int[categoryCount]; // 주제별 할당 수를 같은 인덱스로 관리

        Arrays.fill(allocateCounts, baseCount); // 기본 분배
        Collections.shuffle(categories); // 남은 카드 수를 랜덤하게 분배
        for (int i = 0; i < remain; i++) {
            allocateCounts[i] = allocateCounts[i] + 1;
        }

        // pointer 조회
        List<UserCategory> userCategories = userCategoryRepository.findByUserIdAndCategoryIn(userId, categories);

        // 주제별로 정렬된 카드 리스트에서 pointer 이후 allocation 수만큼 카드 수집
        List<CardResDTO.StudyCard> cards = new ArrayList<>();

        for (int i = 0; i < categoryCount; i++) {
            int needCount = allocateCounts[i];
            int startIndex = 0;
            // user_category에서 해당 주제의 다음 학습 시작 인덱스(pointer) 찾기
            for (int j = 0; j < userCategories.size(); j++) {
                if (userCategories.get(j).getCategory() == categories.get(i)) {
                    startIndex = userCategories.get(j).getPointer();
                    break;
                }
            }

            // 해당 주제의 카드 리스트
            List<Card> cardList = cardRepository.findByCategoryOrderByIdAsc(categories.get(i));
            if (cardList.isEmpty()) {
                throw new CardException(CardErrorCode.Not_Found);
            }

            // pointer 이후 카드들을 needCount 만큼 추출
            int fetchEndIndex = startIndex + needCount;
            if (fetchEndIndex > cardList.size()) { fetchEndIndex = cardList.size(); }// 카드수를 초과하는 경우

            for (int k = startIndex; k < fetchEndIndex; k++) {
                Card card = cardList.get(k);
                List<String> relatedTerms = pickRelatedTerms(cardList, card.getTerm());

                cards.add(
                        CardResDTO.StudyCard.builder()
                                .cardId(card.getId())
                                .category(card.getCategoryType().name())
                                .term(card.getTerm())
                                .descript(card.getDescript())
                                .example(card.getExample())
                                .relatedTerms(relatedTerms)
                                .build()
                );
            }
        }

        // TodayStudySet으로 묶어서 반환
        return CardResDTO.TodayStudySet.builder()
                .studySetId(System.currentTimeMillis())
                .cards(cards)
                .build();
    }

    // 동일한 주제 리스트 내에서 랜덤으로 관련 용어 3개 추출
    private List<String> pickRelatedTerms(List<Card> categoryCards, String currentTerm)
    {
        List<String> terms = new ArrayList<>();
        // 같은 카테고리 카드들에서 용어(term)만 추출
        for (int i = 0; i < categoryCards.size(); i++) {
            Card card = categoryCards.get(i);
            if (!card.getTerm().equals(currentTerm)) {
                terms.add(card.getTerm());
            }
        }
        Collections.shuffle(terms);
        return terms.stream().limit(3)
                .collect(Collectors.toList());
    }
}
