package com.api.biblioteca.client.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.biblioteca.client.model.dto.ClientRequestDTO;
import com.api.biblioteca.client.model.dto.ClientResponseDTO;
import com.api.biblioteca.client.model.entity.ClientEntity;
import com.api.biblioteca.client.model.service.ClientService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody ClientRequestDTO dto){
        try {
            ClientEntity saved = service.create(dto);

            ClientResponseDTO res = new ClientResponseDTO();
            res.setId(saved.getId());
            res.setName(saved.getName());
            res.setPhone(saved.getPhone());
            res.setEmail(saved.getEmail());

            return ResponseEntity.status(201).body(res);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating client");
        }
    }

    @GetMapping
    public List<ClientEntity> AllClients() {
        return service.findAll();
    }
    

}
