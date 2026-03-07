package com.api.biblioteca.loan.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class LoanResponseDTO {
    private UUID id;
    private UUID clientId;
    private UUID bookId;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private LocalDateTime estipulatedReturnDate;

}
