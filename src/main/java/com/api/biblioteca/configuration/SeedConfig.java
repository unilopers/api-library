package com.api.biblioteca.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.biblioteca.book.entity.BookEntity;
import com.api.biblioteca.book.repository.BookRepository;

@Configuration
public class SeedConfig {
    
    @Bean
    CommandLineRunner seedBook(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() > 0) return;

            bookRepository.save(new BookEntity(null, "Clean Code", "Robert C. Martin", 2008, true));
            bookRepository.save(new BookEntity(null, "Effective Java", "Joshua Bloch", 2018, true));
            bookRepository.save(new BookEntity(null, "Domain-Driven Design", "Eric Evans", 2003, true));
            bookRepository.save(new BookEntity(null, "Design Patterns", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides", 1994, true));
            bookRepository.save(new BookEntity(null, "Refactoring", "Martin Fowler", 1999, true));
        
        };
    }
}
