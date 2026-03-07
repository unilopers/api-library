package com.api.biblioteca.loan.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.biblioteca.loan.dto.LoanRequestDTO;
import com.api.biblioteca.loan.dto.LoanResponseDTO;
import com.api.biblioteca.loan.service.LoanService;

@RestController
@RequestMapping("/loan")
public class LoanController {
    
    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping
    public LoanResponseDTO createLoan(@RequestBody LoanRequestDTO request) {
        return service.createLoan(request);
    }

    @GetMapping("/{id}")
    public LoanResponseDTO getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping("/{id}/return")
    public LoanResponseDTO returnLoan(@PathVariable UUID id) {
        return service.returnLoan(id);
    }   
}
