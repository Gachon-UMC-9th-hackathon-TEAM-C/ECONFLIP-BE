package com.example.econflip.domain.user.entity.mapping;

import com.example.econflip.domain.card.entity.Card;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.enums.QuizResult;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user_card")
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_bookmarked", nullable = false)
    private Boolean isBookmarked;

    @Column(name = "dont_know", nullable = false)
    private Boolean dontKnow;

    @Column(name="quiz_result", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuizResult quizResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;
}
