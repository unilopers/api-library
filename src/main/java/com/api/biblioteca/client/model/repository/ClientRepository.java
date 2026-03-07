package com.api.biblioteca.client.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.api.biblioteca.client.model.entity.ClientEntity;

@Repository
public interface ClientRepository extends CrudRepository<ClientEntity, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<ClientEntity> findByEmailIgnoreCase(String email);
    List<ClientEntity> findAll();
}
