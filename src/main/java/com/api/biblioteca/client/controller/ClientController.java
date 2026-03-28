package com.api.biblioteca.client.controller;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.biblioteca.client.model.dto.ClientRequestDTO;
import com.api.biblioteca.client.model.dto.ClientResponseDTO;
import com.api.biblioteca.client.model.entity.ClientEntity;
import com.api.biblioteca.client.model.service.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService service;
    private final RabbitTemplate rabbitTemplate;

    public ClientController(ClientService service, RabbitTemplate rabbitTemplate) {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO dto) {
        try {
            ClientEntity saved = service.create(dto);
            ClientResponseDTO response = toResponse(saved);

            // Publica o email do cliente para processamento assíncrono pós-cadastro.
            rabbitTemplate.convertAndSend("client.queue", response.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @GetMapping
    public List<ClientResponseDTO> getAllClients() {
        return service.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ClientResponseDTO toResponse(ClientEntity client) {
        ClientResponseDTO response = new ClientResponseDTO();
        response.setId(client.getId());
        response.setName(client.getName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        return response;
    }
}
