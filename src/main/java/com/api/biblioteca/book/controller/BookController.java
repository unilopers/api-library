package com.api.biblioteca.book.controller;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.biblioteca.book.dto.BookRequestDTO;
import com.api.biblioteca.book.dto.BookResponseDTO;
import com.api.biblioteca.book.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService service;
    private final RabbitTemplate rabbitTemplate;

    public BookController(BookService service, RabbitTemplate rabbitTemplate) {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/create")
    public ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO request) {
        if (request == null
                || request.getTitle() == null || request.getTitle().isBlank()
                || request.getAuthor() == null || request.getAuthor().isBlank()
                || request.getYear() == null
                || request.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campos obrigatórios: title, author, year, available");
        }

        BookResponseDTO created = service.create(request);
        
        // Enviar para fila de processamento assíncrono
        rabbitTemplate.convertAndSend("book.queue", created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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
