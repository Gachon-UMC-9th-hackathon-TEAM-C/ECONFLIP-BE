package com.example.econflip.domain.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "bookmark")
public class Bookmark{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
