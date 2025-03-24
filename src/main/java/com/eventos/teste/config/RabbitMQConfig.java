package com.eventos.teste.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    @Value("${fila.rabbitmq.queue}")
    private String nomeFila;

    @Bean
    public Queue filaEventos() {
        System.out.println("🐇 Criando fila no RabbitMQ: " + nomeFila);
        return new Queue(nomeFila, true); // true = durável
    }
}
