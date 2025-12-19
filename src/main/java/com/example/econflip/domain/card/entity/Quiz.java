package com.example.econflip.domain.card.entity;

import com.example.econflip.domain.card.enums.QuizType;
import com.example.econflip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "quiz")
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "answer", length = 255, nullable = false)
    private String answer;

    @Column(name = "is_answer", nullable = false)
    private Boolean is_answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuizType quizType;

    @Column(name = "commentary", length = 255, nullable = false)
    private String commentary;
}
