package com.example.econflip.domain.card.service;

import com.example.econflip.domain.card.dto.CardReqDTO;
import com.example.econflip.domain.card.dto.CardResDTO;
import com.example.econflip.domain.card.entity.Card;
import com.example.econflip.domain.card.entity.Quiz;
import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.card.exception.CardException;
import com.example.econflip.domain.card.exception.code.CardErrorCode;
import com.example.econflip.domain.card.repository.CardRepository;
import com.example.econflip.domain.card.repository.QuizRepository;
import com.example.econflip.domain.user.dto.UserCardReqDTO;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.entity.mapping.UserCard;
import com.example.econflip.domain.user.entity.mapping.UserCategory;
import com.example.econflip.domain.user.enums.QuizResult;
import com.example.econflip.domain.user.exception.UserException;
import com.example.econflip.domain.user.exception.code.UserErrorCode;
import com.example.econflip.domain.user.repository.UserCardRepository;
import com.example.econflip.domain.user.repository.UserCategoryRepository;
import com.example.econflip.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final QuizRepository quizRepository;
    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;

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
        boolean isNewStudy = todayUserCards.isEmpty();


        // 3. 새로운 학습 생성
        if (isNewStudy) {
            // 학습분량, 주제개수를 고려해 주제별 카드 개수 분배
            int categoryCount = categories.size();
            int baseCount = user.getDailyStudy() / categoryCount;
            int remain = user.getDailyStudy() % categoryCount; // 균등 분배 후 남는 카드 수

            int[] allocateCounts = new int[categoryCount]; // 주제별 할당 수를 같은 인덱스로 관리
            Arrays.fill(allocateCounts, baseCount); // 기본 분배

            Collections.shuffle(categories); // 남은 카드 수를 랜덤하게 분배
            for (int i = 0; i < remain; i++) {
                allocateCounts[i]++;
            }

            // 사용자 pointer 조회
            List<UserCategory> userCategories = userCategoryRepository.findByUserIdAndCategoryIn(userId, categories);

            for (int i = 0; i < categories.size(); i++) {
                CategoryType category = categories.get(i);
                int needCount = allocateCounts[i];

                int startIndex = userCategories.stream()
                        .filter(userCategory -> userCategory.getCategory() == category)
                        .map(UserCategory::getPointer)
                        .findFirst().orElse(0);

                List<Card> cardList = cardRepository.findByCategoryOrderByIdAsc(category);

                if (cardList.isEmpty()) {
                    throw new CardException(CardErrorCode.CARD_NOT_FOUND);
                }

                int endIndex = Math.min(startIndex + needCount, cardList.size());

                // user_card 생성
                for (int k = startIndex; k < endIndex; k++) {
                    userCardRepository.save(
                            UserCard.builder()
                                    .user(user)
                                    .card(cardList.get(k))
                                    .isConfirmed(false)  // 아직 학습 안함
                                    .isBookmarked(false)
                                    .dontKnow(false)
                                    .quizResult(QuizResult.UNSEEN)
                                    .build()
                    );
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
            List<Quiz> quizList = quizRepository.findByCard(card);
            if(quizList.isEmpty()) {
                throw new CardException(CardErrorCode.QUIZ_NOT_FOUND);
            }
            List<CardResDTO.QuizChoice> choices = new ArrayList<>();
            for (int m = 0; m < quizList.size(); m++) {
                CardResDTO.QuizChoice choice =
                        CardResDTO.QuizChoice.builder()
                                .answerId(quizList.get(m).getId())
                                .answer(quizList.get(m).getAnswer())
                                .build();

                choices.add(choice);
            }
            Collections.shuffle(choices); // 보기 순서 섞기

            quizzes.add(
                    CardResDTO.QuizQuestion.builder()
                            .cardId(card.getId())
                            .question(card.getQuiz())
                            .quizType(quizList.get(0).getQuizType().name())
                            .choices(choices)
                            .commentary(quizList.get(0).getCommentary())
                            .build()
            );
        }

        // 5. TodayStudySet 반환
        return CardResDTO.TodayStudySet.builder()
                .studySetId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)
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
        if(dto.dontKnow()) {
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

        Quiz choiceQuiz = quizRepository.findById(answer.answerId())
                .orElseThrow(() -> new CardException(CardErrorCode.QUIZ_NOT_FOUND));
        Quiz correctQuiz = quizRepository.findCorrectQuizByCardId(cardId)
                .orElseThrow(() -> new CardException(CardErrorCode.QUIZ_NOT_FOUND));

        boolean isCorrect = false;
        if(correctQuiz.getId().equals(answer.answerId())) {
            userCard.updateQuizResult(QuizResult.CORRECT);
            isCorrect = true;
        } else {
            userCard.updateQuizResult(QuizResult.WRONG);
        }

        return CardResDTO.QuizAnswer.builder()
                .isCorrect(isCorrect)
                .commentary(choiceQuiz.getCommentary())
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
}
