package com.api.biblioteca.book.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.api.biblioteca.book.dto.BookResponseDTO;

@Component
public class BookProcessingListener {
    
    @RabbitListener(queues = "book.queue")
    public void processBook(BookResponseDTO book) {
        // Aqui você pode adicionar qualquer processamento após criação do livro
        // Exemplos: enviar email, atualizar cache, indexar em search, gerar relatórios, etc
        System.out.println("Processando livro assincronamente: " + book.getTitle());
    }
}
