package com.api.biblioteca.client.model.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.biblioteca.client.model.dto.ClientRequestDTO;
import com.api.biblioteca.client.model.entity.ClientEntity;
import com.api.biblioteca.client.model.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ClientEntity create(ClientRequestDTO dto) {
        String email = dto.getEmail() == null ? null : dto.getEmail().trim().toLowerCase();
        String name  = dto.getName()  == null ? null : dto.getName().trim();
        String phone = dto.getPhone() == null ? null : dto.getPhone().trim();

        if (repository.existsByEmailIgnoreCase(email)) { 
            throw new IllegalArgumentException("email já cadastrado");
        }

        ClientEntity client = new ClientEntity();
        client.setName(name);
        client.setPhone(phone);
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(dto.getPassword()));

        return repository.save(client);
    }

    public List<ClientEntity> findAll() {
        return repository.findAll();
    }

}
