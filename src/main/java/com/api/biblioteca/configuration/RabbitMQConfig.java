package com.api.biblioteca.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Bean
    public Queue bookQueue() {
        return new Queue("book.queue", true);
    }

    @Bean
    public Queue clientQueue() {
        return new Queue("client.queue", true);
    }
}
