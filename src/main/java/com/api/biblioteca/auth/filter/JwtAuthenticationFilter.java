package com.api.biblioteca.auth.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.biblioteca.auth.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        //Lê o header Authorization enviado na requisição.
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            //Remove o prefixo "Bearer " para obter apenas o token JWT.
            String token = authHeader.substring(BEARER_PREFIX.length());

            try {
                if (tokenService.isTokenValid(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //Extrai o usuário definido no token.
                    String subject = tokenService.extractSubject(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //Registra a autenticação no contexto do Spring Security.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                //Token inválido/malformado: segue sem autenticar a requisição.
                SecurityContextHolder.clearContext();
            }
        }

        //Continua a cadeia de filtros independentemente do resultado da autenticação.
        filterChain.doFilter(request, response);
    }
}
