package com.api.biblioteca.loan.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.biblioteca.book.entity.BookEntity;
import com.api.biblioteca.book.repository.BookRepository;
import com.api.biblioteca.client.model.entity.ClientEntity;
import com.api.biblioteca.client.model.repository.ClientRepository;
import com.api.biblioteca.loan.dto.LoanRequestDTO;
import com.api.biblioteca.loan.dto.LoanResponseDTO;
import com.api.biblioteca.loan.entity.LoanEntity;
import com.api.biblioteca.loan.repository.LoanRepository;

import jakarta.transaction.Transactional;

@Service
public class LoanService {
    
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;

    public LoanService(
            LoanRepository loanRepository,
            BookRepository bookRepository,
            ClientRepository clientRepository
    ) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO request) {
        if (request.getClientId() == null || request.getBookId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "clientId e bookId são obrigatórios"
            );
        }

        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"
                ));

        BookEntity book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado"
                ));

        if (Boolean.FALSE.equals(book.getAvailable())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Livro indisponível para empréstimo"
            );
        }

        loanRepository.findByBook_IdAndReturnDateIsNull(book.getId())
                .ifPresent(loan -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Este livro já está emprestado"
                    );
                });

        LoanEntity loan = new LoanEntity();
        loan.setClient(client);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setEstipulatedReturnDate(LocalDateTime.now().plusDays(7));
        loan.setReturnDate(null);

        book.setAvailable(false);

        bookRepository.save(book);
        LoanEntity savedLoan = loanRepository.save(loan);

        return toResponse(savedLoan);
    }

    @Transactional
    public LoanResponseDTO returnLoan(UUID id) {
        LoanEntity loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Empréstimo não encontrado"
                ));

        if (loan.getReturnDate() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este empréstimo já foi devolvido"
            );
        }

        loan.setReturnDate(LocalDateTime.now());

        BookEntity book = loan.getBook();
        book.setAvailable(true);

        bookRepository.save(book);
        LoanEntity updatedLoan = loanRepository.save(loan);

        return toResponse(updatedLoan);
    }

    public LoanResponseDTO findById(UUID id) {
        LoanEntity loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Empréstimo não encontrado"
                ));

        return toResponse(loan);
    }

    private LoanResponseDTO toResponse(LoanEntity loan) {
        LoanResponseDTO dto = new LoanResponseDTO();
        dto.setId(loan.getId());
        dto.setClientId(loan.getClient().getId());
        dto.setBookId(loan.getBook().getId());
        dto.setLoanDate(loan.getLoanDate());
        dto.setEstipulatedReturnDate(loan.getEstipulatedReturnDate());
        dto.setReturnDate(loan.getReturnDate());
        return dto;
    }
}
