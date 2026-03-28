package com.api.biblioteca.client.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClientProcessingListener {

    @RabbitListener(queues = "client.queue")
    public void processClientEmail(String email) {
        // Ponto simples para processamentos pós-cadastro (ex.: envio de email).
        System.out.println("Processando cliente assincronamente para email: " + email);
    }
}
