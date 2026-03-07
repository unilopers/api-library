package com.api.biblioteca.loan.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.api.biblioteca.book.entity.BookEntity;
import com.api.biblioteca.client.model.entity.ClientEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "loan")
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private BookEntity book;

    private LocalDateTime loanDate;

    private LocalDateTime returnDate;
    
    private LocalDateTime estipulatedReturnDate;
}
