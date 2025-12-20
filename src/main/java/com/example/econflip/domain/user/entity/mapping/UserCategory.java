package com.example.econflip.domain.user.entity.mapping;

import com.example.econflip.domain.card.enums.CategoryType;
import com.example.econflip.domain.user.entity.User;
import com.example.econflip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "user_category")
public class UserCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pointer", nullable = false)
    @Builder.Default
    private Integer pointer = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private CategoryType category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updatePointer(Integer pointer) {
        this.pointer = pointer;
    }
}
