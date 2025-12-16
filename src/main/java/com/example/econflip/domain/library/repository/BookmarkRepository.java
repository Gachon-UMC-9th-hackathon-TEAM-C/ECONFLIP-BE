package com.example.econflip.domain.library.repository;

import com.example.econflip.domain.library.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
