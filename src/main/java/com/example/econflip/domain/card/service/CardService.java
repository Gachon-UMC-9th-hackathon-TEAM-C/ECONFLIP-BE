package com.example.econflip.domain.card.service;

import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.entity.Card;
import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.card.exception.CardException;
import com.example.econflip.domain.card.exception.code.CardErrorCode;
import com.example.econflip.domain.card.repository.CardRepository;
import com.example.econflip.domain.user.dto.UserResDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.entity.mapping.UserCard;
import com.example.econflip.domain.user.entity.mapping.UserCategory;
import com.example.econflip.domain.user.enums.QuizResult;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.UserCardRepository;
import com.example.econflip.domain.user.repository.UserCategoryRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import com.example.econflip.domain.user.service.BadgeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final BadgeService badgeService;

    // 오늘의 학습 세트 시작/복구 API
    @Transactional
    public CardResDTO.TodayStudySet startTodayStudySet(Long userId, List<String> selectedCategories)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND));

        // 1. 학습에 사용할 Category 목록
        List<CategoryType> categories = new ArrayList<>();
        if (selectedCategories == null || selectedCategories.isEmpty()) {
            categories.addAll(Arrays.asList(CategoryType.values()));
        } else {
            try {
                categories = selectedCategories.stream()
                        .map(CategoryType::valueOf)
                        .toList();
            } catch (IllegalArgumentException e) {
                throw new CardException(CardErrorCode.CATEGORY_NOT_FOUND);
            }
        }

        // 2. 오늘 생성된 user_card 존재 여부 확인 => (학습 복구 or 새 학습 시작)
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        List<UserCard> todayUserCards = userCardRepository.findByUserIdAndCreatedAtBetween(userId, start, end);

        // 오늘 이미 생성된 카드 ID 목록 추출 (중복 방지용)
        Set<Long> existingCardIds = todayUserCards.stream()
                .map(userCard -> userCard.getCard().getId())
                .collect(Collectors.toSet());

        boolean isNewStudy = todayUserCards.isEmpty();


        // 3. 새로운 학습 생성
        if (isNewStudy) {
            // 학습분량, 주제개수를 고려해 주제별 카드 개수 분배
            int categoryCount = categories.size();
            int baseCount = user.getDailyStudy() / categoryCount;
            int remain = user.getDailyStudy() % categoryCount; // 균등 분배 후 남는 카드 수

            // 카테고리별 할당 개수를 Map으로 관리 (카테고리 순서와 무관하게 처리)
            Map<CategoryType, Integer> allocateCountsMap = new HashMap<>();
            for (CategoryType category : categories) {
                allocateCountsMap.put(category, baseCount);
            }

            // 남은 카드 수를 랜덤하게 분배하기 위해 categories를 섞어서 앞쪽부터 추가
            List<CategoryType> shuffledCategories = new ArrayList<>(categories);
            Collections.shuffle(shuffledCategories);
            for (int i = 0; i < remain; i++) {
                CategoryType category = shuffledCategories.get(i);
                allocateCountsMap.put(category, allocateCountsMap.get(category) + 1);
            }

            // 사용자 pointer 조회 (UserCategory가 없으면 생성)
            List<UserCategory> userCategories = userCategoryRepository.findByUserIdAndCategoryIn(userId, categories);
            Map<CategoryType, UserCategory> userCategoryMap = new HashMap<>();

            // 기존 UserCategory를 Map에 저장
            for (UserCategory userCategory : userCategories) {
                userCategoryMap.put(userCategory.getCategory(), userCategory);
            }

            // UserCategory가 없는 카테고리는 생성
            for (CategoryType category : categories) {
                if (!userCategoryMap.containsKey(category)) {
                    UserCategory newUserCategory = UserCategory.builder()
                            .user(user)
                            .category(category)
                            .pointer(0)
                            .build();
                    userCategoryMap.put(category, userCategoryRepository.save(newUserCategory));
                }
            }

            // 각 카테고리별로 카드 생성
            for (CategoryType category : categories) {
                int needCount = allocateCountsMap.get(category);
                UserCategory userCategory = userCategoryMap.get(category);
                int startIndex = userCategory.getPointer();

                List<Card> cardList = cardRepository.findByCategoryOrderByIdAsc(category);

                if (cardList.isEmpty()) {
                    throw new CardException(CardErrorCode.CARD_NOT_FOUND);
                }

                int endIndex = Math.min(startIndex + needCount, cardList.size());

                // user_card 생성 (중복 체크)
                for (int k = startIndex; k < endIndex; k++) {
                    Card card = cardList.get(k);
                    // 오늘 이미 생성된 카드가 아닌 경우에만 생성
                    if (!existingCardIds.contains(card.getId())) {
                        userCardRepository.save(
                                UserCard.builder()
                                        .user(user)
                                        .card(card)
                                        .isConfirmed(false)  // 아직 학습 안함
                                        .isBookmarked(false)
                                        .dontKnow(false)
                                        .quizResult(QuizResult.UNSEEN)
                                        .build()
                        );
                        existingCardIds.add(card.getId()); // 생성한 카드 ID 추가
                    }
                }
            }

            // 새로 생성한 user_card 다시 조회
            todayUserCards = userCardRepository.findByUserIdAndCreatedAtBetween(userId, start, end);
        }

        // 4. is_confirmed=false(학습완료되지 않음)인 카드부터 DTO 리스트 구성
        List<CardResDTO.StudyCard> cards = new ArrayList<>(); // pointer 이후 allocation 수만큼 카드 수집
        List<CardResDTO.QuizQuestion> quizzes = new ArrayList<>(); // 카드id에 해당하는 퀴즈 수집

        List<UserCard> remainingCards = new ArrayList<>(); // 아직 학습이 완료되지 않은 카드들
        for (UserCard userCard : todayUserCards) { // 오늘 생성된 user_card들을 하나씩 확인
            if (!userCard.isConfirmed()) { // 학습 완료되지 않은 카드만 추출
                remainingCards.add(userCard);
            }
        }

        // 아직 학습하지 않은 카드들로 학습 이어서 진행
        for(UserCard userCard : remainingCards) {
            Card card = userCard.getCard();

            // 카드 DTO
            List<Card> cardList = cardRepository.findByCategoryOrderByIdAsc(card.getCategory());
            List<String> relatedTerms = pickRelatedTerms(cardList, card.getTerm()); // 관련 용어
            cards.add(
                    CardResDTO.StudyCard.builder()
                            .cardId(card.getId())
                            .category(card.getCategory().name())
                            .term(card.getTerm())
                            .descript(card.getDescript())
                            .example(card.getExample())
                            .tip(card.getTip())
                            .relatedTerms(relatedTerms)
                            .build()
            );

            // 퀴즈 DTO
            // 보기 생성
            List<String> choices = new ArrayList<>();
            choices.add(card.getTerm()); // 정답

            List<String> wrongTerms = pickRelatedTerms(
                    cardRepository.findByCategoryOrderByIdAsc(card.getCategory()),
                    card.getTerm()
            );
            choices.addAll(wrongTerms);
            Collections.shuffle(choices);
            quizzes.add(
                    CardResDTO.QuizQuestion.builder()
                            .cardId(card.getId())
                            .question(pickRandomQuiz(card))
                            .choices(choices.stream().map(term ->
                                    CardResDTO.QuizChoice.builder()
                                            .term(term)
                                            .build()).toList()).build()
            );
        }

        // 5. TodayStudySet 반환
        return CardResDTO.TodayStudySet.builder()
                .cards(cards)
                .quizzes(quizzes)
                .build();
    }

    // 카드 학습 완료 처리 API
    @Transactional
    public void confirmCard(Long userId, Long cardId, UserCardReqDTO.DontKnowReqDTO dto) {
        UserCard userCard = findTodayUserCard(userId, cardId);
        if(!userCard.isConfirmed()) {
            userCard.confirm();
        }
        if(dto != null && dto.dontKnow()) {
            userCard.dontknow();
        }
    }

    // 퀴즈 답안 채점 및 저장 API
    @Transactional
    public CardResDTO.QuizAnswer submitQuizAnswer(Long userId, Long cardId, CardReqDTO.QuizAnswer answer)
    {
        UserCard userCard = findTodayUserCard(userId, cardId);

        // 이미 푼 퀴즈
        if(userCard.getQuizResult() != QuizResult.UNSEEN) {
            throw new CardException(CardErrorCode.QUIZ_ALREADY_ANSWERED);
        }

        Card card = userCard.getCard();

        // null 체크
        if(answer == null || answer.selectedTerm() == null) {
            throw new CardException(CardErrorCode.CARD_NOT_FOUND);
        }

        boolean isCorrect = card.getTerm().equals(answer.selectedTerm());

        if(isCorrect) {
            userCard.updateQuizResult(QuizResult.CORRECT);
        } else {
            userCard.updateQuizResult(QuizResult.WRONG);
        }

        return CardResDTO.QuizAnswer.builder()
                .isCorrect(isCorrect)
                .commentary(card.getDescript())
                .correctAnswer(card.getTerm())
                .build();
    }

    @Transactional
    public CardResDTO.StudyComplete completeTodayStudy(User user) {
        if(user.getIsLearned()) { // 이미 학습 완료된 상태 예외처리
            throw new CardException(CardErrorCode.STUDY_ALREADY_FINISHED);
        }

        // 오늘의 user_card 조회
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        List<UserCard> todayUserCards = userCardRepository.findByUserIdAndCreatedAtBetween(user.getId(), start, end);

        // 학습 완료 여부 검증 처리
        boolean hasUnconfirmedCard = todayUserCards.stream().anyMatch(userCard -> !userCard.isConfirmed());
        boolean hasUnsolvedQuiz = todayUserCards.stream()
                .anyMatch(userCard -> userCard.getQuizResult() == QuizResult.UNSEEN);

        if (hasUnconfirmedCard) { // 학습 완료 상태가 아닌 카드 존재 예외처리
            throw new CardException(CardErrorCode.STUDY_CARD_NOT_FINISHED);
        }
        if (hasUnsolvedQuiz) { // 아직 풀지 않은 퀴즈 존재 예외처리
            throw new CardException(CardErrorCode.STUDY_QUIZ_NOT_FINISHED);
        }

        // 퀴즈 결과 집계
        List<String> correctTerms = new ArrayList<>();
        List<String> wrongTerms = new ArrayList<>();
        int correctCount = 0;

        for (UserCard userCard : todayUserCards) {
            if (userCard.getQuizResult() == QuizResult.CORRECT) {
                correctCount++;
                correctTerms.add(userCard.getCard().getTerm());
            } else if (userCard.getQuizResult() == QuizResult.WRONG) {
                wrongTerms.add(userCard.getCard().getTerm());
            }
        }

        // User XP, 레벨, streak, is_learned 업데이트
        int gainedXp = correctCount * 10; // 한 문제당 10xp
        int totalXp = user.getXp() + gainedXp;
        int gainedLevel = totalXp / 50 - user.getXp() / 50;
        user.completeTodayStudy(totalXp, gainedLevel);

        // pointer 업데이트
        // 오늘 학습한 카드들을 카테고리별로 그룹화
        Map<CategoryType, Long> learnedCountByCategory = todayUserCards.stream()
                .filter(UserCard::isConfirmed)
                .collect(Collectors.groupingBy(userCard -> userCard.getCard().getCategory(),
                        Collectors.counting()));

        // 해당 카테고리의 UserCategory 조회
        List<UserCategory> userCategories = userCategoryRepository.findByUserIdAndCategoryIn(user.getId(),
                new ArrayList<>(learnedCountByCategory.keySet()));

        // 카테고리별 pointer 업데이트
        for (UserCategory userCategory : userCategories) {
            CategoryType category = userCategory.getCategory();
            int currentPointer = userCategory.getPointer();
            long learnedCount = learnedCountByCategory.get(category);

            userCategory.updatePointer(currentPointer + (int) learnedCount);
        }
        // 획득 가능한 뱃지 리스트 확인 및 저장
        // 오늘의 총 퀴즈 개수 (퀴즈를 푼 카드 수)
        int todayTotalCount = (int) todayUserCards.stream()
                .filter(userCard -> userCard.getQuizResult() != QuizResult.UNSEEN).count();

        // 획득 가능한 모든 뱃지 체크
        List<UserResDTO.BadgeInfo> newBadges = badgeService.checkAndGiveStudyCompleteBadges(
                user,
                todayUserCards,
                correctCount,
                todayTotalCount
        );
        // 마지막 학습 일자 업데이트
        user.updateLastStudyDate(LocalDate.now());
        int totalStudyCount = todayUserCards.size();

        return CardResDTO.StudyComplete.builder()
                .correctCount(correctCount)
                .gainedXp(gainedXp)
                .totalStudyCount(totalStudyCount)
                .correctTerms(correctTerms)
                .wrongTerms(wrongTerms)
                .newBadges(newBadges)
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

    // 오늘 생성된 UserCard 데이터 조회
    private UserCard findTodayUserCard(Long userId, Long cardId) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return userCardRepository
                .findByUserIdAndCardIdAndCreatedAtBetween(userId, cardId, start, end)
                .orElseThrow(() -> new CardException(CardErrorCode.CARD_NOT_FOUND));
    }

    // 두 개 타입의 퀴즈 문제(quiz_fill_blank, quiz_case) 중 랜덤하게 한개 선택
    private String pickRandomQuiz(Card card) {
        return ThreadLocalRandom.current().nextBoolean()
                ? card.getQuizFillBlank()
                : card.getQuizCase();
    }
}
