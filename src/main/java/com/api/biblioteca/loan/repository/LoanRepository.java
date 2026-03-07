package com.api.biblioteca.loan.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.biblioteca.loan.entity.LoanEntity;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {
    Optional<LoanEntity> findByBook_IdAndReturnDateIsNull(UUID bookId);
    List<LoanEntity> findByClient_Id(UUID clientId);
}
