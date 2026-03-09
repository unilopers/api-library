package com.api.biblioteca.auth.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(String subject) {
        // Define instante atual e data de expiração do token.
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(expirationMs);

        // Gera o JWT assinado com subject, emissão e expiração.
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractSubject(String token) {
        // Valida assinatura e extrai o subject do payload.
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            // Se conseguir extrair o subject, considera o token válido.
            extractSubject(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Qualquer erro de parse/assinatura/expiração invalida o token.
            return false;
        }
    }

    private SecretKey getSigningKey() {
        // Cria a chave HMAC a partir do secret configurado.
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
