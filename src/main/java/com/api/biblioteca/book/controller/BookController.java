package com.api.biblioteca.book.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.biblioteca.book.dto.BookResponseDTO;
import com.api.biblioteca.book.repository.BookRepository;
import com.api.biblioteca.book.service.BookService;





@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService service;

    public BookController(BookRepository repository) {
        this.service = new BookService(repository);
    }

    @GetMapping
    public List<BookResponseDTO> getAll() {
        List<BookResponseDTO> books = service.findAll();
        
        if(books == null || books.isEmpty()) {
            throw new RuntimeException("Não há livros cadastrados");
        }
        
        return books;
    }

    @GetMapping("/{id}")
    public BookResponseDTO getById(@PathVariable UUID id) {
        BookResponseDTO book = service.findById(id);
        
        if(book == null) {
            throw new RuntimeException("Livro não encontrado");
        }
        
        return book;
    }

    @GetMapping("/search")
    public List<BookResponseDTO> getByTitle(@RequestParam(required=false) String title) {
        if(title == null || title.isBlank()) {
            List<BookResponseDTO> books = service.findAll();
            return books;
        }
        
        List<BookResponseDTO> books = service.findByTitleContainingIgnoreCase(title.trim());
        
        if(books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum livro encontrado com o título: " + title);
        }
        
        return books;
    }
    
    
}
