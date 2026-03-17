package com.api.biblioteca.book.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.biblioteca.book.dto.BookRequestDTO;
import com.api.biblioteca.book.dto.BookResponseDTO;
import com.api.biblioteca.book.entity.BookEntity;
import com.api.biblioteca.book.repository.BookRepository;


@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public BookResponseDTO create(BookRequestDTO bookRequestDTO) {
        if(repository.existsByTitle(bookRequestDTO.getTitle())) {
            throw new RuntimeException("Já existe um livro com o título: " + bookRequestDTO.getTitle());
        }

        BookEntity entity = new BookEntity();

        entity.setTitle(bookRequestDTO.getTitle());
        entity.setAuthor(bookRequestDTO.getAuthor());
        entity.setYear(bookRequestDTO.getYear());
        entity.setAvailable(bookRequestDTO.getAvailable());

        BookEntity savedEntity = repository.save(entity);
        return toResponse(savedEntity);
    }

    public List<BookResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public List<BookResponseDTO> findByTitleContainingIgnoreCase(String title) {
        return repository.findByTitleContainingIgnoreCase(title).stream().map(this::toResponse).toList();
    }

    public List<BookResponseDTO> findByAvailable(Boolean available) {
        return repository.findByAvailable(available).stream().map(this::toResponse).toList();
    }

    public List<BookResponseDTO> findByTitleContainingIgnoreCaseAndAvailable(String title, Boolean available) {
        return repository.findByTitleContainingIgnoreCaseAndAvailable(title, available).stream().map(this::toResponse).toList();
    }

    private BookResponseDTO toResponse(BookEntity e) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(e.getId().toString());
        dto.setTitle(e.getTitle());
        dto.setAuthor(e.getAuthor());
        dto.setYear(e.getYear());
        dto.setAvailable(e.getAvailable());
        return dto;
    }
}
