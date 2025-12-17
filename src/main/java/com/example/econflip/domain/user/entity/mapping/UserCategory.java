package com.example.econflip.domain.user.entity.mapping;

import com.example.econflip.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "user_category")
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pointer", nullable = false)
    private Integer pointer;

    @Column(name = "category", nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
