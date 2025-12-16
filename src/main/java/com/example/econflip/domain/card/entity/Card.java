package com.example.econflip.domain.card.entity;

import com.example.econflip.domain.card.enums.TagType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "card")
public class Card{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TagType tag;

    @Column(name = "term", length = 100, nullable = false)
    private String term;

    @Column(name = "quiz", length = 255, nullable = false)
    private String quiz;

    @Column(name = "descript", length = 255, nullable = false)
    private String descript;

    @Column(name = "tip", length = 255, nullable = false)
    private String tip;

    @Column(name = "example", length = 255, nullable = false)
    private String example;
}
