package com.api.biblioteca.book.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.biblioteca.book.dto.BookResponseDTO;
import com.api.biblioteca.book.entity.BookEntity;
import com.api.biblioteca.book.repository.BookRepository;


@Service
public class BookService {

    
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public BookResponseDTO findById(UUID id) {
        BookEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        return toResponse(entity);
    }

    public List<BookResponseDTO> findByAvailable(Boolean available) {
        return repository.findByAvailable(available).stream().map(this::toResponse).toList();
    }

    public List<BookResponseDTO> findByTitleContainingIgnoreCase(String title) {
        return repository.findByTitleContainingIgnoreCase(title).stream().map(this::toResponse).toList();
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
