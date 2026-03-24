package com.api.biblioteca.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.biblioteca.auth.dto.LoginRequestDTO;
import com.api.biblioteca.auth.dto.LoginResponseDTO;
import com.api.biblioteca.auth.service.TokenService;
import com.api.biblioteca.client.model.entity.ClientEntity;
import com.api.biblioteca.client.model.repository.ClientRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Value("${security.jwt.expiration-ms}")
    private Long expirationMs;

    public AuthController(
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService
    ) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        //Normaliza email para evitar falhas por maiúsculas/espaços.
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        //Busca o usuário e retorna 401 se não existir.
        ClientEntity client = clientRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        //Compara a senha informada com o hash salvo no banco.
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), client.getPassword());
        if (!passwordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }

        //Gera o JWT e devolve no formato esperado pelo cliente.
        String token = tokenService.generateToken(client.getEmail());
        LoginResponseDTO response = new LoginResponseDTO(token, "Bearer", expirationMs);

        return ResponseEntity.ok(response);
    }
}
