package com.api.biblioteca.loan.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class LoanRequestDTO {
    private UUID clientId;
    private UUID bookId;
}
