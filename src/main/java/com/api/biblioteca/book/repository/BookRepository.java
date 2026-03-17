package com.api.biblioteca.book.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.biblioteca.book.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, UUID> {
    boolean existsByTitle(String title);
    List<BookEntity> findByAvailable(Boolean available);
    List<BookEntity> findByTitleContainingIgnoreCase(String title);
    List<BookEntity> findByTitleContainingIgnoreCaseAndAvailable(String title, Boolean available);
}
