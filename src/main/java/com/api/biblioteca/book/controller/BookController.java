package com.api.biblioteca.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.biblioteca.book.dto.BookResponseDTO;
import com.api.biblioteca.book.service.BookService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.biblioteca.book.dto.BookRequestDTO;


@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public BookResponseDTO post(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO createdBook = service.create(bookRequestDTO);
        return createdBook;
    }
    

    @GetMapping
    public List<BookResponseDTO> getAll() {
        List<BookResponseDTO> books = service.findAll();
        
        if(books == null || books.isEmpty()) {
            throw new RuntimeException("Não há livros cadastrados");
        }
        
        return books;
    }

    @GetMapping("/title")
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
    
    @GetMapping("/available")
    public List<BookResponseDTO> getByAvailable(@RequestParam(required=false) Boolean available) {
        if(available == null) {
            return service.findAll();
        }
        return service.findByAvailable(available);
    } 

    @GetMapping("/search")
    public List<BookResponseDTO> searchTitleAvailable(@RequestParam(required=false) String title, @RequestParam(required=false) Boolean available) {
        
        if(title != null && !title.isBlank() && available != null) {
            List<BookResponseDTO> books = service.findByTitleContainingIgnoreCaseAndAvailable(title.trim(), available);
                
            if(books.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum livro encontrado com o título: " + title + " e disponibilidade: " + available);
            }
                
            return books;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetros 'title' e 'available' são obrigatórios para esta busca");
        }
    }
    
}
