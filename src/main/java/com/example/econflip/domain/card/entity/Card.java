package com.example.econflip.domain.card.entity;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "card")
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoryType category;

    @Column(name = "term", length = 100, nullable = false)
    private String term;

    @Column(name = "descript", length = 255, nullable = false)
    private String descript;

    @Column(name = "tip", length = 255, nullable = false)
    private String tip;

    @Column(name = "example", length = 255, nullable = false)
    private String example;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;

    @Column(name = "quiz_fill_blank", length = 255, nullable = false)
    private String quiz_fill_blank;

    @Column(name = "quiz_case", length = 255, nullable = false)
    private String quiz_case;
}
